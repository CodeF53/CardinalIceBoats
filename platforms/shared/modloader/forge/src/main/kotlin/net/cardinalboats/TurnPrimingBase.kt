package net.cardinalboats

import net.cardinalboats.alias.KEY_BINDING_CATEGORY_REG
import net.cardinalboats.generated.ModInfo
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent
import kotlin.concurrent.atomics.ExperimentalAtomicApi

interface TurnPrimingBase {

    val lQueueKey: KeyMapping
    val rQueueKey: KeyMapping
    val smartCenterKey: KeyMapping

    @OptIn(ExperimentalAtomicApi::class)
    fun init() {

        ClientTickEvent.Post.BUS.addListener { event: ClientTickEvent.Post ->
            tick(Minecraft.getInstance())
        }
        RegisterKeyMappingsEvent.BUS.addListener { event: RegisterKeyMappingsEvent ->
            KEY_BINDING_CATEGORY_REG.compareAndSet(null,
                                                   KeyMapping.Category(Identifier.fromNamespaceAndPath(ModInfo.MOD_ID,
                                                                                                       "binding_category")))
            event.register(lQueueKey)
            event.register(rQueueKey)
            event.register(smartCenterKey)
        }
    }

    fun tick(minecraft: Minecraft)

}
