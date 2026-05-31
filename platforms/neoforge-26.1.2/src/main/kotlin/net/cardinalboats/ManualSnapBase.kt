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
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

interface ManualSnapBase {
    val manualSnapKey: KeyMapping
    val snap180: KeyMapping


    fun tick(minecraft: Minecraft)

    // Run by fabric initializer
    fun init() {

        FORGE_BUS.addListener { event: ClientTickEvent.Post ->
            tick(Minecraft.getInstance())
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
