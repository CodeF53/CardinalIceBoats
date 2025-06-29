package net.cardinalboats

import net.cardinalboats.config.CIBConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.block.AirBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.vehicle.AbstractBoatEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction.*
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import java.util.function.Function

@Suppress("MagicNumber")
object TurnPriming {
    @JvmField
    val lQueueKey = KeyBinding(
        "key.cardinalboats.prime_left",
        InputUtil.Type.KEYSYM,
        InputUtil.GLFW_KEY_LEFT,
        "category.cardinalboats.key_category_title"
    )

    @JvmField
    val rQueueKey = KeyBinding(
        "key.cardinalboats.prime_right",
        InputUtil.Type.KEYSYM,
        InputUtil.GLFW_KEY_RIGHT,
        "category.cardinalboats.key_category_title"
    )

    @JvmField
    val smartCenterKey = KeyBinding(
        "key.cardinalboats.smartCenter",
        InputUtil.Type.KEYSYM,
        InputUtil.GLFW_KEY_BACKSLASH,
        "category.cardinalboats.key_category_title"
    )

    // Run by fabric initializer
    fun init() {
        KeyBindingHelper.registerKeyBinding(lQueueKey)
        KeyBindingHelper.registerKeyBinding(rQueueKey)
        KeyBindingHelper.registerKeyBinding(smartCenterKey)

        ClientTickEvents.END_CLIENT_TICK.register { minecraft -> tick(minecraft) }
    }

    private var lTurnPrimed = false
    private var rTurnPrimed = false

    private val toScanMapLeft = mapOf(
        SOUTH to arrayOf(intArrayOf(3, 0), intArrayOf(3, -1), intArrayOf(3, -2)),
        NORTH to arrayOf(intArrayOf(-3, 0), intArrayOf(-3, 1), intArrayOf(-3, 2)),
        EAST to arrayOf(intArrayOf(0, -3), intArrayOf(-1, -3), intArrayOf(-2, -3)),
        WEST to arrayOf(intArrayOf(0, 3), intArrayOf(1, 3), intArrayOf(2, 3))
    )

    private val toScanMapRight = mapOf(
        SOUTH to arrayOf(intArrayOf(-3, 0), intArrayOf(-3, -1), intArrayOf(-3, -2)),
        NORTH to arrayOf(intArrayOf(3, 0), intArrayOf(3, 1), intArrayOf(3, 2)),
        EAST to arrayOf(intArrayOf(0, 3), intArrayOf(-1, 3), intArrayOf(-2, 3)),
        WEST to arrayOf(intArrayOf(0, -3), intArrayOf(1, -3), intArrayOf(2, -3))
    )

    private val snapBlockMap = mapOf(
        SOUTH to arrayOf(intArrayOf(0, 0), intArrayOf(0, -1), intArrayOf(0, -2)),
        NORTH to arrayOf(intArrayOf(0, 0), intArrayOf(0, 1), intArrayOf(0, 2)),
        EAST to arrayOf(intArrayOf(0, 0), intArrayOf(-1, 0), intArrayOf(-2, 0)),
        WEST to arrayOf(intArrayOf(0, 0), intArrayOf(1, 0), intArrayOf(2, 0))
    )

    @Suppress("EmptyWhileBlock", "MagicNumber", "CyclomaticComplexMethod")
    fun tick(minecraft: MinecraftClient) {
        val player = minecraft.player
        if (player != null && player.hasVehicle() && player.vehicle is AbstractBoatEntity) {
            val boat = player.vehicle as AbstractBoatEntity
            if (isIce(boat.steppingBlockState)) {
                while (lQueueKey.wasPressed()) {
                    clientChatLog(player, Text.translatable("info.cardinalboats.left_turn_queue").string)
                    lTurnPrimed = true
                    rTurnPrimed = false
                }
                while (rQueueKey.wasPressed()) {
                    clientChatLog(player, Text.translatable("info.cardinalboats.right_turn_queue").string)
                    rTurnPrimed = true
                    lTurnPrimed = false
                }

                if (CIBConfig.getInstance().alwaysSmartCenter && boat.yaw % 90 == 0f) {
                    smartCenter(boat) 
                }

                while (smartCenterKey.wasPressed()) { 
                    smartCenter(boat) 
                }

                val world = minecraft.world!!

                if (lTurnPrimed && shouldTurn(boat, world, true)) {
                    rotateBoat(boat, roundYRot(boat.yaw - 90, 90), CIBConfig.getInstance().maintainVelocityOnTurns)
                    lTurnPrimed = false
                    clientChatLog(player, Text.translatable("info.cardinalboats.left_turn_complete").string)
                    if (CIBConfig.getInstance().smartCenterPrimedTurn) smartCenter(boat)
                } else if (rTurnPrimed && shouldTurn(boat, world, false)) {
                    rotateBoat(boat, roundYRot(boat.yaw + 90, 90), CIBConfig.getInstance().maintainVelocityOnTurns)
                    rTurnPrimed = false
                    clientChatLog(player, Text.translatable("info.cardinalboats.right_turn_complete").string)
                    if (CIBConfig.getInstance().smartCenterPrimedTurn) smartCenter(boat)
                }
            } else {
                while (lQueueKey.wasPressed() || rQueueKey.wasPressed() || smartCenterKey.wasPressed()) {}
            }
        } else {
            // if we aren't in the boat anymore, we don't care
            if (lTurnPrimed || rTurnPrimed) {
                clientChatLog(minecraft.player, Text.translatable("info.cardinalboats.cancel").string)
            }
            lTurnPrimed = false
            rTurnPrimed = false

            // not in a boat, don't care about any presses these buttons get right now
            while (lQueueKey.wasPressed() || rQueueKey.wasPressed() || smartCenterKey.wasPressed()) {}
        }
    }

    fun shouldTurn(boat: AbstractBoatEntity, level: ClientWorld, left: Boolean): Boolean {
        val rootX = boat.blockX
        val rootY = boat.blockY - 1
        val rootZ = boat.blockZ

        // get the direction the boat is facing
        // north/south/east/west
        val direction = boat.horizontalFacing
        val map: Array<IntArray>

        // get the block offsets for left/right
        map = if (left) {
            toScanMapLeft[direction]!!
        } else {
            toScanMapRight[direction]!!
        }

        for (i in map.indices) {
            val testBlockPos = BlockPos(rootX + map[i][0], rootY, rootZ + map[i][1])
            if (isIce(level.getBlockState(testBlockPos))) {
                val snapBlock = snapBlockMap[direction]!![i]
                boat.setPosition(rootX + snapBlock[0] + 0.5, boat.y, rootZ + snapBlock[1] + 0.5)
                return true
            }
        }

        return false
    }

    fun smartCenter(boat: AbstractBoatEntity) {
        val world = boat.world
        val direction = boat.horizontalFacing
        val rootX = boat.blockX
        val rootY = boat.blockY
        val rootZ = boat.blockZ

        val scanAhead = CIBConfig.getInstance().smartCenterLookAhead
        if (direction == NORTH || direction == SOUTH) {
            val startZ = if (direction == NORTH) -scanAhead else -1
            val endZ = if (direction == NORTH) 1 else scanAhead
            val nudgeX = calculateNudge(world, startZ, endZ, 
                { z -> BlockPos(rootX - 1, rootY, rootZ + z) }, 
                { z -> BlockPos(rootX + 1, rootY, rootZ + z) }
            )
            boat.setPosition(rootX + 0.5 + nudgeX, boat.y, boat.z)
        } else {
            val startX = if (direction == WEST) -scanAhead else -1
            val endX = if (direction == WEST) 1 else scanAhead
            val nudgeZ = calculateNudge(world, startX, endX, 
                { x -> BlockPos(rootX + x, rootY, rootZ - 1) }, 
                { x -> BlockPos(rootX + x, rootY, rootZ + 1) }
            )
            boat.setPosition(boat.x, boat.y, rootZ + 0.5 + nudgeZ)
        }
    }

    private fun calculateNudge(world: World, start: Int, end: Int, leftBlockPosFunc: Function<Int, BlockPos>, rightBlockPosFunc: Function<Int, BlockPos>): Double {
        var nudge = 0
        for (i in start..end) {
            val leftBlockPos = leftBlockPosFunc.apply(i)
            val rightBlockPos = rightBlockPosFunc.apply(i)
            if (world.getBlockState(leftBlockPos).block !is AirBlock)
                nudge += 1
            if (world.getBlockState(rightBlockPos).block !is AirBlock)
                nudge -= 1
        }
        return MathHelper.clamp(nudge.toDouble(), -0.2, 0.2)
    }
}
