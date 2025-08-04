package net.cardinalboats

import net.cardinalboats.alias.FORGE_BUS
import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.MinecraftClient
import net.neoforged.neoforge.client.event.ClientTickEvent
import org.apache.commons.lang3.ArrayUtils


interface ManualSnapBase {

    val manualSnapKey: KeyBinding
    val snap180: KeyBinding

    fun tick(minecraft: MinecraftClient)

    // Run by fabric initializer
    fun init() {

        FORGE_BUS.addListener { event: ClientTickEvent.Post ->
            tick(MinecraftClient.getInstance())
        }

        keyMappings = ArrayUtils.addAll(keyMappings, manualSnapKey, snap180);
    }
}
