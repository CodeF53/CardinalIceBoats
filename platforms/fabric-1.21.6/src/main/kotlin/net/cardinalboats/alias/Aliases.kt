package net.cardinalboats.alias

import com.mojang.logging.LogUtils
import net.minecraft.block.AirBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.client.world.ClientWorld
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.vehicle.AbstractBoatEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.block.BlockState
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d



typealias AirBlock = AirBlock
typealias ClientWorld = ClientWorld
typealias ClientPlayerEntity = ClientPlayerEntity
typealias PlayerEntity = PlayerEntity
typealias World = World
typealias MinecraftClient = MinecraftClient
typealias AbstractBoatEntity = AbstractBoatEntity
typealias Text = Text
typealias BlockPos = BlockPos
typealias BlockState = BlockState
typealias Direction = Direction
typealias MathHelper = MathHelper
typealias InputUtil = InputUtil
typealias InputUtilType = InputUtil.Type
typealias KeyBinding = KeyBinding
typealias HitResult = HitResult
typealias HitResultType = HitResult.Type
typealias BlockHitResult = BlockHitResult
typealias Vec3d = Vec3d
typealias LogUtils = LogUtils

val GLFW_KEY_UP = InputUtil.GLFW_KEY_UP
val GLFW_KEY_DOWN = InputUtil.GLFW_KEY_DOWN
val GLFW_KEY_LEFT = InputUtil.GLFW_KEY_LEFT
val GLFW_KEY_RIGHT = InputUtil.GLFW_KEY_RIGHT
val GLFW_KEY_BACKSLASH = InputUtil.GLFW_KEY_BACKSLASH

val RADIANS_PER_DEGREE = MathHelper.RADIANS_PER_DEGREE
