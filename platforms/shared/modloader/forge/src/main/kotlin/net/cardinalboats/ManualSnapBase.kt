package net.cardinalboats

import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent

interface ManualSnapBase {
    val manualSnapKey: KeyMapping
    val snap180: KeyMapping



    fun tick(minecraft: Minecraft)

    // Run by fabric initializer
    fun init() {

        RegisterKeyMappingsEvent.BUS.addListener { event ->
            event.register(manualSnapKey)
            event.register(snap180)
        }

        ClientTickEvent.Post.BUS.addListener { event ->
            tick(Minecraft.getInstance())
        }

    }

}
