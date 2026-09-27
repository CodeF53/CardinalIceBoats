package net.cardinalboats


import net.cardinalboats.alias.KEY_BINDING_CATEGORY
import net.cardinalboats.alias.KEY_BINDING_CATEGORY_REG
import net.cardinalboats.generated.ModInfo
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import net.neoforged.fml.ModLoadingContext
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.common.NeoForge
import org.anti_ad.mc.common.extensions.ifTrue
import kotlin.concurrent.atomics.ExperimentalAtomicApi

interface ManualSnapBase {
    val manualSnapKey: KeyMapping
    val snap180: KeyMapping


    fun tick(minecraft: Minecraft)

    // Run by fabric initializer
    @OptIn(ExperimentalAtomicApi::class)
    fun init() {

        val modBus = ModLoadingContext.get().activeContainer.eventBus

        modBus?.addListener { event: RegisterKeyMappingsEvent ->
            KEY_BINDING_CATEGORY_REG.compareAndSet(null,
                                                   KeyMapping.Category(Identifier.fromNamespaceAndPath(ModInfo.MOD_ID,
                                                                                                       "binding_category"))).ifTrue {
                event.registerCategory(KEY_BINDING_CATEGORY_REG.load()!!)
            }
            // Register your keybinding
            event.register(manualSnapKey)
            event.register(snap180)
            // Register other keybindings here
        }

        NeoForge.EVENT_BUS.addListener { event: ClientTickEvent.Post ->
            tick(Minecraft.getInstance())
        }

    }

}
