package net.cardinalboats

import net.cardinalboats.config.ConfigScreenSettings
import net.cardinalboats.generated.ModInfo.MOD_ID
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.fml.IExtensionPoint
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.fml.loading.FMLEnvironment
import org.anti_ad.mc.common.gui.screen.ConfigScreenBase

@Suppress("UnusedParameter")
@Mod(MOD_ID)
class CardinalBoatsInit(val context: FMLJavaModLoadingContext) {

    init {
        if (FMLEnvironment.dist.isClient) {
            TurnPriming.init()
            ManualSnap.init()

            context.registerExtensionPoint(IExtensionPoint.DisplayTest::class.java) {
                IExtensionPoint.DisplayTest({ context.container.modInfo.version.toString() }) { _: String?, _: Boolean? ->
                    true
                }
            }
            context.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
                ConfigScreenHandler.ConfigScreenFactory { _: Minecraft?, p: Screen? ->
                    ConfigScreenBase(ConfigScreenSettings).apply {
                        parent = p
                        dumpWidgetTree()
                    }
                }
            }
        }
    }
}



