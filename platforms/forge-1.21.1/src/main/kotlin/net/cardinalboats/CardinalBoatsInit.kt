package net.cardinalboats

import com.llamalad7.mixinextras.MixinExtrasBootstrap
import me.shedaniel.autoconfig.AutoConfig
import net.cardinalboats.config.CIBConfig
import net.cardinalboats.generated.ModInfo
import net.cardinalboats.generated.ModInfo.MOD_ID
import net.minecraft.client.Minecraft
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.IExtensionPoint
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.runForDist

@Suppress("UnusedParameter")
@Mod(MOD_ID)
class CardinalBoatsInit {

    init {

        runForDist(
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


            thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT.registerExtensionPoint(IExtensionPoint.DisplayTest::class.java) {
                IExtensionPoint.DisplayTest({ thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT.container.modInfo.version.toString() }) {
                        _: String?, _: Boolean? -> true
                }
            }

            thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
                ConfigScreenHandler.ConfigScreenFactory { _: Minecraft?, p: net.minecraft.client.gui.screens.Screen? ->
                    AutoConfig.getConfigScreen(CIBConfig::class.java, p).get()
                }
            }
        } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
            @Suppress("PrintStackTrace")
            t.printStackTrace()
        }
    }

    /**
     * Fired on the global Forge bus.
     */
    @Suppress("EmptyFunctionBlock")
    @SubscribeEvent
    fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
    }

    @Suppress("EmptyFunctionBlock")
    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
    }
}
