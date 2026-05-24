package net.cardinalboats

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.InputUtilType
import net.cardinalboats.alias.InputUtil
import net.cardinalboats.alias.MinecraftClient

interface ManualSnapBase {
    val manualSnapKey: KeyBinding
    val snap180: KeyBinding


    fun tick(minecraft: MinecraftClient)

    // Run by fabric initializer
    fun init() {
        KeyBindingHelper.registerKeyBinding(manualSnapKey)
        KeyBindingHelper.registerKeyBinding(snap180)

        ClientTickEvents.END_CLIENT_TICK.register { minecraft ->
            tick(minecraft)
        }

    }

}
