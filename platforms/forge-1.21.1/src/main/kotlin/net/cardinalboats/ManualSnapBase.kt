package net.cardinalboats


import net.cardinalboats.ManualSnap.manualSnapKey
import net.cardinalboats.ManualSnap.snap180
import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.MinecraftClient
import net.cardinalboats.generated.ModInfo
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

interface ManualSnapBase {
    val manualSnapKey: KeyBinding
    val snap180: KeyBinding


    fun tick(minecraft: MinecraftClient)

    // Run by fabric initializer
    fun init() {

        FORGE_BUS.addListener { event: TickEvent.ClientTickEvent ->
            tick(MinecraftClient.getInstance())
        }

    }

    @Mod.EventBusSubscriber(modid = ModInfo.MOD_ID)
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
