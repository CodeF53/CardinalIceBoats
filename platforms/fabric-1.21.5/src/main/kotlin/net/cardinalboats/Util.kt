package net.cardinalboats

import net.cardinalboats.config.CIBConfig
import net.minecraft.block.BlockState
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.vehicle.AbstractBoatEntity
import net.minecraft.text.Text
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.regex.Pattern

import net.minecraft.util.math.MathHelper.RADIANS_PER_DEGREE

private val icePattern = Pattern.compile("(\\b|_)ice\\b", Pattern.CASE_INSENSITIVE)

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
}

fun isIce(blockState: BlockState): Boolean {
    return icePattern.matcher(blockState.block.toString()).find()
}

fun clientChatLog(player: ClientPlayerEntity?, message: String) {
    if (player == null) return

    if (CIBConfig.getInstance().doChatShit) {
        player.sendMessage(Text.of("[cardinalboats] $message"), false)
    }
}

@Suppress("MagicNumber")
fun shouldSnap(level: World, player: PlayerEntity): Boolean {
    // If we are putting a boat on a block
    val lookingAt = player.raycast(20.0, 0.0f, false)
    if (lookingAt != null && lookingAt.type == HitResult.Type.BLOCK) {
        // If that block is ice, return true
        return isIce(level.getBlockState((lookingAt as BlockHitResult).blockPos))
    }
    return false
}

@Suppress("MagicNumber")
fun roundYRot(yRot: Float, toNearest: Int): Float {
    return (Math.round(yRot % 360 / toNearest) * toNearest).toFloat()
}
