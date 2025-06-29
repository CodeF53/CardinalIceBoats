package net.cardinalboats

import net.cardinalboats.config.CIBConfig
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.vehicle.AbstractBoatEntity

object ManualSnap {

    val manualSnapKey = KeyBinding(
        "key.cardinalboats.snapManual",
        InputUtil.Type.KEYSYM,
        InputUtil.GLFW_KEY_UP,
        "category.cardinalboats.key_category_title"
    )

    val snap180 = KeyBinding(
        "key.cardinalboats.snap180",
        InputUtil.Type.KEYSYM,
        InputUtil.GLFW_KEY_DOWN,
        "category.cardinalboats.key_category_title"
    )

    // Run by fabric initializer
    fun init() {
        KeyBindingHelper.registerKeyBinding(manualSnapKey)
        KeyBindingHelper.registerKeyBinding(snap180)

        ClientTickEvents.END_CLIENT_TICK.register(ManualSnap::tick)
    }

    @Suppress("EmptyWhileBlock", "MagicNumber")
    fun tick(minecraft: MinecraftClient) {
        val player = minecraft.player
        if (player != null && player.hasVehicle() && player.vehicle is AbstractBoatEntity) {
            val boat = player.vehicle as AbstractBoatEntity
            if (isIce(boat.steppingBlockState)) {
                while (manualSnapKey.wasPressed()) {
                    val snapAngle = if (CIBConfig.getInstance().eightWaySnapKey) 45 else 90
                    rotateBoat(boat, roundYRot(boat.yaw, snapAngle), true)
                }
                while (snap180.wasPressed()) {
                    rotateBoat(boat, boat.yaw % 360 - 180, CIBConfig.getInstance().maintainVelocityOnTurns)
                }
            }
        } else {
            while (manualSnapKey.wasPressed() || snap180.wasPressed()) {}
        }
    }
}
