package net.cardinalboats

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

interface TurnPrimingBase {

    val lQueueKey: KeyMapping
    val rQueueKey: KeyMapping
    val smartCenterKey: KeyMapping

    fun init() {
        KeyMappingHelper.registerKeyMapping(lQueueKey)
        KeyMappingHelper.registerKeyMapping(rQueueKey)
        KeyMappingHelper.registerKeyMapping(smartCenterKey)

        ClientTickEvents.END_CLIENT_TICK.register { minecraft -> tick(minecraft) }
    }

    fun tick(minecraft: Minecraft)
}
