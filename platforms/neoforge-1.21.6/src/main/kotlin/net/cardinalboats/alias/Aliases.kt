package net.cardinalboats.alias

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.logging.LogUtils
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.vehicle.AbstractBoat
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

typealias AirBlock = AirBlock
typealias ClientWorld = ClientLevel
typealias ClientPlayerEntity = LocalPlayer
typealias PlayerEntity = Player
typealias World = Level
typealias MinecraftClient = Minecraft
typealias AbstractBoatEntity = AbstractBoat
typealias Text = Component
typealias BlockPos = BlockPos
typealias BlockState = BlockState
typealias Direction = Direction
typealias MathHelper = Mth
typealias InputUtil = InputConstants
typealias InputUtilType = InputConstants.Type
typealias KeyBinding = KeyMapping
typealias HitResult= HitResult
typealias HitResultType = HitResult.Type
typealias BlockHitResult = BlockHitResult
typealias Vec3d = Vec3
typealias LogUtils = LogUtils

val GLFW_KEY_UP = InputConstants.KEY_UP
val GLFW_KEY_DOWN = InputConstants.KEY_DOWN

val GLFW_KEY_LEFT = InputConstants.KEY_LEFT
val GLFW_KEY_RIGHT = InputConstants.KEY_RIGHT
val GLFW_KEY_BACKSLASH = InputConstants.KEY_BACKSLASH


val Entity.steppingBlockState
    get() = this.blockStateOn

var Entity.yaw
    get() = this.yRot
    set(value) { this.yRot = value }

var AbstractBoat.yawVelocity
    get() = this.deltaRotation
    set(value) { this.deltaRotation = value }


fun KeyBinding.wasPressed(): Boolean {
    return this.consumeClick()
}

val MinecraftClient.world: ClientWorld?
    get() = this.level

val Entity.horizontalFacing
    get() = this.direction

fun Entity.setPosition(x: Double, y: Double, z: Double) = this.setPos(x, y, z)

val Entity.world
    get() = this.level()

var Entity.velocity: Vec3d
    get() {
        return this.getDeltaMovement()
    }
    set(value) {
        this.setDeltaMovement(value)
    }

fun Entity.raycast(d: Double, f: Float, b: Boolean) = this.pick(d, f, b)

@JvmField
@Suppress("MagicNumber")
val RADIANS_PER_DEGREE = (Math.PI.toFloat() / 180f);

fun Vec3.rotateY(y: Float) = this.yRot(y)

fun LocalPlayer.sendMessage(text: Text, actionBar: Boolean) = this.displayClientMessage(text, actionBar)
