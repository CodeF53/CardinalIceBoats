package net.cardinalboats

import net.cardinalboats.alias.RADIANS_PER_DEGREE
import net.cardinalboats.config.CIBConfig
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.boat.AbstractBoat
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

import java.util.regex.Pattern
import kotlin.math.roundToInt

private val icePattern = Pattern.compile("(\\b|_)ice\\b", Pattern.CASE_INSENSITIVE)


@JvmField
var lieAboutMovingForward = false;

var Entity.velocity: Vec3
    get() {
        return this.getDeltaMovement()
    }
    set(value) {
        this.setDeltaMovement(value)
    }

fun rotateBoat(boat: AbstractBoat, rotation: Float, maintainVelocity: Boolean, postAction: () -> Unit = {}) {

    if (maintainVelocity) {
        // get current velocity vector length
        val currentVelocity = boat.velocity.length()
        // create new vector normalized to rotation
        val newVelocity = Vec3(0.0, 0.0, currentVelocity).yRot(-rotation * RADIANS_PER_DEGREE) // Trig magic
        // give boat new thing
        boat.velocity = newVelocity
    } else {
        boat.velocity = Vec3.ZERO
    }
    boat.yRot = rotation
    boat.deltaRotation = 0f
    boat.controllingPassenger?.yRot = boat.yRot

    postAction()
}

fun isIce(blockState: BlockState): Boolean {
    if (icePattern.matcher(blockState.block.toString()).find()) {
        return true
    } else {
        return false
    }
}

fun clientChatLog(player: Player?, message: String) {
    if (player == null) return

    if (CIBConfig.getInstance().doChatShit) {
        player.sendOverlayMessage(Component.literal("[cardinalboats] $message"))
    }
}

@Suppress("MagicNumber")
fun shouldSnap(level: Level, player: Player): Boolean {
    // If we are putting a boat on a block
    val lookingAt: HitResult? = player.pick(20.0, 0.0f, false)
    if (lookingAt != null && lookingAt.type == HitResult.Type.BLOCK) {
        // If that block is ice, return true
        return isIce(level.getBlockState((lookingAt as BlockHitResult).blockPos))
    }
    return false
}

@Suppress("MagicNumber")
fun roundYRot(yRot: Float, toNearest: Int): Float {
    return ((yRot % 360 / toNearest).roundToInt() * toNearest).toFloat()
}

