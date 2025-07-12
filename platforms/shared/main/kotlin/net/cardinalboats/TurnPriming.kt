package net.cardinalboats

import net.cardinalboats.config.CIBConfig
import net.cardinalboats.alias.*
import net.minecraft.world.waypoints.WaypointTransmitter

@Suppress("MagicNumber")
object TurnPriming: TurnPrimingBase {

    private val logger = LogUtils.getLogger()

    override val lQueueKey = KeyBinding(
        "key.cardinalboats.prime_left",
        InputUtilType.KEYSYM,
        GLFW_KEY_LEFT,
        "category.cardinalboats.key_category_title"
    )

    override val rQueueKey = KeyBinding(
        "key.cardinalboats.prime_right",
        InputUtilType.KEYSYM,
        GLFW_KEY_RIGHT,
        "category.cardinalboats.key_category_title"
    )


    override val smartCenterKey = KeyBinding(
        "key.cardinalboats.smartCenter",
        InputUtilType.KEYSYM,
        GLFW_KEY_BACKSLASH,
        "category.cardinalboats.key_category_title"
    )

    private var lTurnPrimed = false
    private var rTurnPrimed = false

    private val toScanMapLeft = mapOf(
        Direction.SOUTH to arrayOf(intArrayOf(3, 0), intArrayOf(3, -1), intArrayOf(3, -2)),
        Direction.NORTH to arrayOf(intArrayOf(-3, 0), intArrayOf(-3, 1), intArrayOf(-3, 2)),
        Direction.EAST to arrayOf(intArrayOf(0, -3), intArrayOf(-1, -3), intArrayOf(-2, -3)),
        Direction.WEST to arrayOf(intArrayOf(0, 3), intArrayOf(1, 3), intArrayOf(2, 3))
    )

    private val toScanMapRight = mapOf(
        Direction.SOUTH to arrayOf(intArrayOf(-3, 0), intArrayOf(-3, -1), intArrayOf(-3, -2)),
        Direction.NORTH to arrayOf(intArrayOf(3, 0), intArrayOf(3, 1), intArrayOf(3, 2)),
        Direction.EAST to arrayOf(intArrayOf(0, 3), intArrayOf(-1, 3), intArrayOf(-2, 3)),
        Direction.WEST to arrayOf(intArrayOf(0, -3), intArrayOf(1, -3), intArrayOf(2, -3))
    )

    private val snapBlockMap = mapOf(
        Direction.SOUTH to arrayOf(intArrayOf(0, 0), intArrayOf(0, -1), intArrayOf(0, -2)),
        Direction.NORTH to arrayOf(intArrayOf(0, 0), intArrayOf(0, 1), intArrayOf(0, 2)),
        Direction.EAST to arrayOf(intArrayOf(0, 0), intArrayOf(-1, 0), intArrayOf(-2, 0)),
        Direction.WEST to arrayOf(intArrayOf(0, 0), intArrayOf(1, 0), intArrayOf(2, 0))
    )

    @Suppress("EmptyWhileBlock", "MagicNumber", "CyclomaticComplexMethod")
    override fun tick(minecraft: MinecraftClient) {
        val player = minecraft.player
        if (player != null && player.vehicle != null && player.vehicle is AbstractBoatEntity) {
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
                boat.setPosition(rootX + snapBlock[0] + 0.5, boat.y, rootZ + snapBlock[1] + 0.5)
                lieAboutMovingForward = false
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
            boat.setPosition(rootX + 0.5 + nudgeX, boat.y, boat.z)
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
            boat.setPosition(boat.x, boat.y, rootZ + 0.5 + nudgeZ)
        }
    }

    private fun calculateNudge(world: World,
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
        return MathHelper.clamp(nudge.toDouble(), -0.2, 0.2)
    }
}
