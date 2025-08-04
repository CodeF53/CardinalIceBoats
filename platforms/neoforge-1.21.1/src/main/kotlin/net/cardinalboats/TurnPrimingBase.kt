package net.cardinalboats

import net.cardinalboats.alias.ClientTickEventPost
import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.MinecraftClient

import org.apache.commons.lang3.ArrayUtils
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

interface TurnPrimingBase {

    val lQueueKey: KeyBinding
    val rQueueKey: KeyBinding
    val smartCenterKey: KeyBinding

    fun tick(minecraft: MinecraftClient)

    fun init() {

        FORGE_BUS.addListener { event: ClientTickEventPost ->
            tick(MinecraftClient.getInstance())
        }

        keyMappings = ArrayUtils.addAll(keyMappings, lQueueKey, rQueueKey, smartCenterKey)
    }
}
