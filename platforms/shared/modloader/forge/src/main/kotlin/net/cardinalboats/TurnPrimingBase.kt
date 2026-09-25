package net.cardinalboats

import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent

interface TurnPrimingBase {

    val lQueueKey: KeyMapping
    val rQueueKey: KeyMapping
    val smartCenterKey: KeyMapping

    fun init() {

        ClientTickEvent.Post.BUS.addListener { event: ClientTickEvent.Post ->
            tick(Minecraft.getInstance())
        }
        RegisterKeyMappingsEvent.BUS.addListener { event: RegisterKeyMappingsEvent ->
            event.register(lQueueKey)
            event.register(rQueueKey)
            event.register(smartCenterKey)
        }
    }

    fun tick(minecraft: Minecraft)

}
