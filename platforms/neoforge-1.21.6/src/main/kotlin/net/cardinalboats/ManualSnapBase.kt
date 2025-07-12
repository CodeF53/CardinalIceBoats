package net.cardinalboats


import net.cardinalboats.ManualSnap.manualSnapKey
import net.cardinalboats.ManualSnap.snap180
import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.MinecraftClient
import net.iceboats.generated.ModInfo
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

interface ManualSnapBase {
    val manualSnapKey: KeyBinding
    val snap180: KeyBinding


    fun tick(minecraft: MinecraftClient)

    // Run by fabric initializer
    fun init() {

        FORGE_BUS.addListener { event: ClientTickEvent.Post ->
            tick(MinecraftClient.getInstance())
        }

    }

    @EventBusSubscriber(modid = ModInfo.MOD_ID)
    companion object {
        @SubscribeEvent
        fun onKeyRegister(event: RegisterKeyMappingsEvent) {
            // Register your keybinding
            event.register(manualSnapKey)
            event.register(snap180)
            // Register other keybindings here
        }
    }

}
