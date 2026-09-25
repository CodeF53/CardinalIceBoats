package net.cardinalboats

import com.google.common.eventbus.Subscribe
import net.cardinalboats.TurnPriming.lQueueKey
import net.cardinalboats.TurnPriming.rQueueKey
import net.cardinalboats.TurnPriming.smartCenterKey
import net.cardinalboats.generated.ModInfo
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

interface TurnPrimingBase {

    val lQueueKey: KeyMapping
    val rQueueKey: KeyMapping
    val smartCenterKey: KeyMapping

    fun init() {

        MinecraftForge.EVENT_BUS.register { event: ClientTickEvent.Post ->
            tick(Minecraft.getInstance())
        }
        MinecraftForge.EVENT_BUS.register(Companion)
    }

    fun tick(minecraft: Minecraft)

    @EventBusSubscriber(modid = ModInfo.MOD_ID)
    companion object {
        @Subscribe
        fun onKeyRegister(event: RegisterKeyMappingsEvent) {
            // Register your keybinding
            event.register(lQueueKey)
            event.register(rQueueKey)
            event.register(smartCenterKey)
            // Register other keybindings here
        }
    }}
