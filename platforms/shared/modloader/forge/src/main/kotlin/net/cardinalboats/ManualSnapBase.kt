package net.cardinalboats

import net.cardinalboats.alias.KEY_BINDING_CATEGORY_REG
import net.cardinalboats.generated.ModInfo
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.event.TickEvent.ClientTickEvent
import kotlin.concurrent.atomics.ExperimentalAtomicApi

interface ManualSnapBase {
    val manualSnapKey: KeyMapping
    val snap180: KeyMapping



    fun tick(minecraft: Minecraft)

    // Run by fabric initializer
    @OptIn(ExperimentalAtomicApi::class)
    fun init() {

        RegisterKeyMappingsEvent.BUS.addListener { event ->
            KEY_BINDING_CATEGORY_REG.compareAndSet(null,
                                                   KeyMapping.Category(Identifier.fromNamespaceAndPath(ModInfo.MOD_ID,
                                                                                                       "binding_category")))
            event.register(manualSnapKey)
            event.register(snap180)
        }

        ClientTickEvent.Post.BUS.addListener { event ->
            tick(Minecraft.getInstance())
        }

    }

}
