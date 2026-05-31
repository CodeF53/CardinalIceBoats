package net.cardinalboats

import com.mojang.blaze3d.platform.InputConstants
import net.cardinalboats.alias.KEY_BINDING_CATEGORY
import net.cardinalboats.config.CIBConfig
import net.minecraft.client.KeyMapping
import net.minecraft.client.KeyMapping.Category
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.entity.vehicle.boat.AbstractBoat
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AirBlock

@Suppress("MagicNumber")
object TurnPriming: TurnPrimingBase {

    private class TickCountingTask(private var ticks: Int? = null,
                                   private var times: Int? = null,
                                   val task: () -> Unit) {
        init {
            if (ticks == null) {
                ticks = CIBConfig.getInstance().smartCenterPrimedTurnDelayTicks
            }
            if (times == null) {
                times = 0
            }
        }

        fun tick(): Boolean {
            ticks = ticks!! - 1
            if (ticks!! <= 0) {
                task()
                times = times!! - 1
                if (times!! <= 0) {
                    return true
                } else {
                    ticks = CIBConfig.getInstance().smartCenterPrimedTurnDelayTicks
                }
            }
            return false
        }

        fun runNow() {
            task()
        }
    }

    private val tasks = mutableListOf<TickCountingTask>()

    private fun TickCountingTask.addTask(): TickCountingTask {
        synchronized(tasks) {
            tasks.add(this)
        }
        return this;
    }

    override val lQueueKey = KeyMapping("key.cardinalboats.prime_left",
                                        InputConstants.Type.KEYSYM,
                                        InputConstants.KEY_LEFT,
                                        KEY_BINDING_CATEGORY) //"category.cardinalboats.key_category_title"

    override val rQueueKey = KeyMapping("key.cardinalboats.prime_right",
                                        InputConstants.Type.KEYSYM,
                                        InputConstants.KEY_RIGHT,
                                        KEY_BINDING_CATEGORY
        //"category.cardinalboats.key_category_title"
    )


    override val smartCenterKey = KeyMapping("key.cardinalboats.smartCenter",
                                             InputConstants.Type.KEYSYM,
                                             InputConstants.KEY_BACKSLASH,
                                             KEY_BINDING_CATEGORY
                                            //"category.cardinalboats.key_category_title"
    )

    private var lTurnPrimed = false
    private var rTurnPrimed = false

    private val toScanMapLeft = mapOf(Direction.SOUTH to arrayOf(intArrayOf(3, 0), intArrayOf(3, -1), intArrayOf(3, -2)),
                                      Direction.NORTH to arrayOf(intArrayOf(-3, 0), intArrayOf(-3, 1), intArrayOf(-3, 2)),
                                      Direction.EAST to arrayOf(intArrayOf(0, -3), intArrayOf(-1, -3), intArrayOf(-2, -3)),
                                      Direction.WEST to arrayOf(intArrayOf(0, 3), intArrayOf(1, 3), intArrayOf(2, 3)))

    private val toScanMapRight = mapOf(Direction.SOUTH to arrayOf(intArrayOf(-3, 0), intArrayOf(-3, -1), intArrayOf(-3, -2)),
                                       Direction.NORTH to arrayOf(intArrayOf(3, 0), intArrayOf(3, 1), intArrayOf(3, 2)),
                                       Direction.EAST to arrayOf(intArrayOf(0, 3), intArrayOf(-1, 3), intArrayOf(-2, 3)),
                                       Direction.WEST to arrayOf(intArrayOf(0, -3), intArrayOf(1, -3), intArrayOf(2, -3)))

    private val snapBlockMap = mapOf(Direction.SOUTH to arrayOf(intArrayOf(0, 0), intArrayOf(0, -1), intArrayOf(0, -2)),
                                     Direction.NORTH to arrayOf(intArrayOf(0, 0), intArrayOf(0, 1), intArrayOf(0, 2)),
                                     Direction.EAST to arrayOf(intArrayOf(0, 0), intArrayOf(-1, 0), intArrayOf(-2, 0)),
                                     Direction.WEST to arrayOf(intArrayOf(0, 0), intArrayOf(1, 0), intArrayOf(2, 0)))

    val centerTask = {
        val boat = Minecraft.getInstance().player?.vehicle
        if (boat != null && boat is AbstractBoat) {
            smartCenter(boat)
        }
    }

    @Suppress("EmptyWhileBlock", "MagicNumber", "CyclomaticComplexMethod")
    override fun tick(minecraft: Minecraft) {
        val player = minecraft.player
        if (player != null && player.vehicle != null && player.vehicle is AbstractBoat) {
            tasks.runAll()
            val boat = player.vehicle as AbstractBoat
            if (isIce(boat.blockStateOn)) {
                while (lQueueKey.consumeClick()) {
                    clientChatLog(player, Component.translatable("info.cardinalboats.left_turn_queue").string)
                    lTurnPrimed = true
                    rTurnPrimed = false
                }
                while (rQueueKey.consumeClick()) {
                    clientChatLog(player, Component.translatable("info.cardinalboats.right_turn_queue").string)
                    rTurnPrimed = true
                    lTurnPrimed = false
                }

                if (CIBConfig.getInstance().alwaysSmartCenter && boat.yRot % 90 == 0f) {
                    TickCountingTask(task = centerTask).addTask().runNow()
                }

                while (smartCenterKey.consumeClick()) {
                    TickCountingTask(task = centerTask).addTask().runNow()
                }

                val world = minecraft.level!!

                if (lTurnPrimed && shouldTurn(boat, world, true)) {
                    rotateBoat(boat, roundYRot(boat.yRot - 90, 90), CIBConfig.getInstance().maintainVelocityOnTurns)
                    lTurnPrimed = false
                    clientChatLog(player, Component.translatable("info.cardinalboats.left_turn_complete").string)
                    TickCountingTask {
                        if (CIBConfig.getInstance().smartCenterPrimedTurn) centerTask()
                    }.addTask().runNow()
                } else if (rTurnPrimed && shouldTurn(boat, world, false)) {
                    rotateBoat(boat, roundYRot(boat.yRot + 90, 90), CIBConfig.getInstance().maintainVelocityOnTurns)
                    rTurnPrimed = false
                    clientChatLog(player, Component.translatable("info.cardinalboats.right_turn_complete").string)
                    TickCountingTask {
                        if (CIBConfig.getInstance().smartCenterPrimedTurn) centerTask()
                    }.addTask().runNow()
                }
            } else {
                while (lQueueKey.consumeClick() || rQueueKey.consumeClick() || smartCenterKey.consumeClick()) {}
            }
        } else {
            // if we aren't on the boat any more, we don't care
            if (lTurnPrimed || rTurnPrimed) {
                clientChatLog(minecraft.player, Component.translatable("info.cardinalboats.cancel").string)
            }
            lTurnPrimed = false
            rTurnPrimed = false

            // not in a boat, don't care about any presses these buttons get right now
            while (lQueueKey.consumeClick() || rQueueKey.consumeClick() || smartCenterKey.consumeClick()) {}
        }
    }

    fun shouldTurn(boat: AbstractBoat, level: ClientLevel, left: Boolean): Boolean {
        val rootX = boat.blockX
        val rootY = boat.blockY - 1
        val rootZ = boat.blockZ

        // get the direction the boat is facing
        // north/south/east/west
        val direction = boat.direction
        // get the block offsets for left/right
        val map = if (left) {
            toScanMapLeft[direction]!!
        } else {
            toScanMapRight[direction]!!
        }

        for (i in map.indices) {
            val testBlockPos = BlockPos(rootX + map[i][0], rootY, rootZ + map[i][1])
            if (isIce(level.getBlockState(testBlockPos))) {
                lieAboutMovingForward = true
                val snapBlock = snapBlockMap[direction]!![i]
                boat.setPos(rootX + snapBlock[0] + 0.5, boat.y, rootZ + snapBlock[1] + 0.5)
                lieAboutMovingForward = false
                return true
            }
        }

        return false
    }

    fun smartCenter(boat: AbstractBoat) {
        val world = boat.level()
        val direction = boat.direction
        val rootX = boat.blockX
        val rootY = boat.blockY
        val rootZ = boat.blockZ

        val scanAhead = CIBConfig.getInstance().smartCenterLookAhead
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            val startZ = if (direction == Direction.NORTH) -scanAhead else -1
            val endZ = if (direction == Direction.NORTH) 1 else scanAhead
            val nudgeX = calculateNudge(world,
                                        startZ,
                                        endZ,
                                        { z ->
                                            BlockPos(rootX - 1, rootY, rootZ + z)
                                        },
                                        { z ->
                                            BlockPos(rootX + 1, rootY, rootZ + z)
                                        }
            )
            //logger.info("NS setting boat pos to x: ${rootX + 0.5 + nudgeX}, y: ${boat.y}, z: ${boat.z}")
            boat.setPos(rootX + 0.5 + nudgeX, boat.y, boat.z)
        } else {
            val startX = if (direction == Direction.WEST) -scanAhead else -1
            val endX = if (direction == Direction.WEST) 1 else scanAhead
            val nudgeZ = calculateNudge(world,
                                        startX,
                                        endX,
                                        { x ->
                                            BlockPos(rootX + x, rootY, rootZ - 1)
                                        },
                                        { x ->
                                            BlockPos(rootX + x, rootY, rootZ + 1)
                                        }
            )
            //logger.info("setting boat pos to x: ${boat.x}, y: ${boat.x}, z: ${rootZ + 0.5 + nudgeZ}")
            boat.setPos(boat.x, boat.y, rootZ + 0.5 + nudgeZ)
        }
    }

    private fun calculateNudge(world: Level,
                               start: Int,
                               end: Int,
                               leftBlockPosFunc: (Int) -> BlockPos,
                               rightBlockPosFunc: (Int) -> BlockPos): Double {
        var nudge = 0
        for (i in start..end) {
            val leftBlockPos = leftBlockPosFunc(i)
            val rightBlockPos = rightBlockPosFunc(i)
            if (world.getBlockState(leftBlockPos).block !is AirBlock)
                nudge += 1
            if (world.getBlockState(rightBlockPos).block !is AirBlock)
                nudge -= 1
        }
        return Mth.clamp(nudge.toDouble(), -0.2, 0.2)
    }

    private fun MutableList<TickCountingTask>.runAll() {
        synchronized(this) {
            val toRemove = this.filter {
                it.tick()
            }
            this.removeAll(toRemove)
        }
    }

}

