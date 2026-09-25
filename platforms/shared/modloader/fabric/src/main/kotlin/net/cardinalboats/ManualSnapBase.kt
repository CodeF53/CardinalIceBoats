package net.cardinalboats

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

interface ManualSnapBase {
    val manualSnapKey: KeyMapping
    val snap180: KeyMapping


    fun tick(minecraft: Minecraft)

    // Run by fabric initializer
    fun init() {
        KeyMappingHelper.registerKeyMapping(manualSnapKey)
        KeyMappingHelper.registerKeyMapping(snap180)

        ClientTickEvents.END_CLIENT_TICK.register { minecraft ->
            tick(minecraft)
        }

    }

}
