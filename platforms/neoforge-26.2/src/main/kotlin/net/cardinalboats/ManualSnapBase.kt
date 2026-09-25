package net.cardinalboats

import net.cardinalboats.ManualSnap.manualSnapKey
import net.cardinalboats.ManualSnap.snap180
import net.cardinalboats.generated.ModInfo
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.common.NeoForge

interface ManualSnapBase {
    val manualSnapKey: KeyMapping
    val snap180: KeyMapping


    fun tick(minecraft: Minecraft)

    // Run by fabric initializer
    fun init() {
        NeoForge.EVENT_BUS.addListener { event: ClientTickEvent.Post ->
            tick(Minecraft.getInstance())
        }
    }

    @EventBusSubscriber(modid = ModInfo.MOD_ID)
    companion object {
        fun onKeyRegister(event: RegisterKeyMappingsEvent) {
            // Register your keybinding
            event.register(manualSnapKey)
            event.register(snap180)
            // Register other keybindings here
        }
    }

}
