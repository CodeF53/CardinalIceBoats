package net.cardinalboats


import com.google.common.eventbus.Subscribe
import net.cardinalboats.ManualSnap.manualSnapKey
import net.cardinalboats.ManualSnap.snap180
import net.cardinalboats.generated.ModInfo
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

interface ManualSnapBase {
    val manualSnapKey: KeyMapping
    val snap180: KeyMapping


    fun tick(minecraft: Minecraft)

    // Run by fabric initializer
    fun init() {

        MinecraftForge.EVENT_BUS.register{ event: ClientTickEvent.Post ->
            tick(Minecraft.getInstance())
        }

        MinecraftForge.EVENT_BUS.register(Companion)

    }

    @EventBusSubscriber(modid = ModInfo.MOD_ID)
    companion object {
        @Subscribe
        fun onKeyRegister(event: RegisterKeyMappingsEvent) {
            // Register your keybinding
            event.register(manualSnapKey)
            event.register(snap180)
            // Register other keybindings here
        }
    }

}
