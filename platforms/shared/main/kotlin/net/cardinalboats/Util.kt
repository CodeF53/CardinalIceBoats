package net.cardinalboats

import net.cardinalboats.alias.*
import net.cardinalboats.config.CIBConfig

import java.util.regex.Pattern
import kotlin.math.roundToInt

private val icePattern = Pattern.compile("(\\b|_)ice\\b", Pattern.CASE_INSENSITIVE)


private val logger = LogUtils.getLogger()

@JvmField
public var lieAboutMovingForward = false;

fun rotateBoat(boat: AbstractBoatEntity, rotation: Float, maintainVelocity: Boolean) {
    boat.yaw = rotation
    boat.yawVelocity = 0f
    boat.controllingPassenger?.yaw = boat.yaw

    if (maintainVelocity) {
        // get current velocity vector length
        val currentVelocity = boat.velocity.length()
        // create new vector normalized to rotation
        val newVelocity = Vec3d(0.0, 0.0, currentVelocity).rotateY(-rotation * RADIANS_PER_DEGREE) // Trig magic
        // give boat new thing
        boat.velocity = newVelocity
    } else {
        boat.velocity = Vec3d.ZERO
    }
    logger.info("Rotating boat ${boat.uuid} to $rotation, new velocity: ${boat.velocity}")
}

fun isIce(blockState: BlockState): Boolean {
    return icePattern.matcher(blockState.block.toString()).find()
}

fun clientChatLog(player: ClientPlayerEntity?, message: String) {
    if (player == null) return

    if (CIBConfig.getInstance().doChatShit) {
        player.sendMessage(Text.literal("[cardinalboats] $message"), false)
    }
}

@Suppress("MagicNumber")
fun shouldSnap(level: World, player: PlayerEntity): Boolean {
    // If we are putting a boat on a block
    val lookingAt = player.raycast(20.0, 0.0f, false)
    if (lookingAt != null && lookingAt.type == HitResultType.BLOCK) {
        // If that block is ice, return true
        return isIce(level.getBlockState((lookingAt as BlockHitResult).blockPos))
    }
    return false
}

@Suppress("MagicNumber")
fun roundYRot(yRot: Float, toNearest: Int): Float {
    return ((yRot % 360 / toNearest).roundToInt() * toNearest).toFloat()
}

