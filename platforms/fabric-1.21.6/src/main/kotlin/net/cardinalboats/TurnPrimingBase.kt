package net.cardinalboats

import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.MinecraftClient
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper


interface TurnPrimingBase {

    val lQueueKey: KeyBinding
    val rQueueKey: KeyBinding
    val smartCenterKey: KeyBinding

    fun init() {
        KeyBindingHelper.registerKeyBinding(lQueueKey)
        KeyBindingHelper.registerKeyBinding(rQueueKey)
        KeyBindingHelper.registerKeyBinding(smartCenterKey)

        ClientTickEvents.END_CLIENT_TICK.register { minecraft -> tick(minecraft) }
    }

    fun tick(minecraft: MinecraftClient)
}
