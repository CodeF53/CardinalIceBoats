package net.cardinalboats

import net.cardinalboats.TurnPriming.lQueueKey
import net.cardinalboats.TurnPriming.rQueueKey
import net.cardinalboats.TurnPriming.smartCenterKey
import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.MinecraftClient
import net.iceboats.generated.ModInfo
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

interface TurnPrimingBase {

    val lQueueKey: KeyBinding
    val rQueueKey: KeyBinding
    val smartCenterKey: KeyBinding

    fun init() {

        FORGE_BUS.addListener { event: ClientTickEvent.Post ->
            tick(MinecraftClient.getInstance())
        }
    }

    fun tick(minecraft: MinecraftClient)

    @EventBusSubscriber(modid = ModInfo.MOD_ID)
    companion object {
        @SubscribeEvent
        fun onKeyRegister(event: RegisterKeyMappingsEvent) {
            // Register your keybinding
            event.register(lQueueKey)
            event.register(rQueueKey)
            event.register(smartCenterKey)
            // Register other keybindings here
        }
    }}
