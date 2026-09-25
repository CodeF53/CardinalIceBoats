package net.cardinalboats

import net.cardinalboats.TurnPriming.lQueueKey
import net.cardinalboats.TurnPriming.rQueueKey
import net.cardinalboats.TurnPriming.smartCenterKey
import net.cardinalboats.generated.ModInfo
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

interface TurnPrimingBase {

    val lQueueKey: KeyMapping
    val rQueueKey: KeyMapping
    val smartCenterKey: KeyMapping

    fun init() {

        FORGE_BUS.addListener { event: ClientTickEvent.Post ->
            tick(Minecraft.getInstance())
        }
    }

    fun tick(minecraft: Minecraft)

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
