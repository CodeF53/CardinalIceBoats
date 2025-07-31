package net.cardinalboats

import net.cardinalboats.TurnPriming.lQueueKey
import net.cardinalboats.TurnPriming.rQueueKey
import net.cardinalboats.TurnPriming.smartCenterKey
import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.MinecraftClient
import net.cardinalboats.generated.ModInfo
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

interface TurnPrimingBase {

    val lQueueKey: KeyBinding
    val rQueueKey: KeyBinding
    val smartCenterKey: KeyBinding

    fun init() {

        FORGE_BUS.addListener { event: TickEvent.ClientTickEvent ->
            tick(MinecraftClient.getInstance())
        }
    }

    fun tick(minecraft: MinecraftClient)

    @Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
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
