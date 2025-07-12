package net.cardinalboats

import me.shedaniel.autoconfig.AutoConfig
import net.cardinalboats.config.CIBConfig
import net.iceboats.generated.ModInfo
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

@Suppress("ALL")
@Mod(ModInfo.MOD_ID)
class CardinalBoatsInit {

    init {

        val obj = runForDist(
            clientTarget = {
                MOD_BUS.addListener(::onClientSetup)
            },
            serverTarget = {
                MOD_BUS.addListener(::onServerSetup)
                "test"
            })
    }



    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        try {
            TurnPriming.init()
            ManualSnap.init()
            CIBConfig.init()
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory::class.java) {
                IConfigScreenFactory { _, p ->
                    AutoConfig.getConfigScreen(CIBConfig::class.java, p).get()
                }
            }
        } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {

            t.printStackTrace()
        }
    }

    /**
     * Fired on the global Forge bus.
     */
    @Suppress("EmptyFunctionBlock")
    @SubscribeEvent
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
    }

    @Suppress("EmptyFunctionBlock")
    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
    }
}
