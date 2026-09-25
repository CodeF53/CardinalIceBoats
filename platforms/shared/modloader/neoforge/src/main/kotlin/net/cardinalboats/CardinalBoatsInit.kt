package net.cardinalboats

import net.cardinalboats.config.ConfigScreenSettings
import net.cardinalboats.generated.ModInfo
import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import org.anti_ad.mc.common.gui.screen.ConfigScreenBase

@Suppress("ALL")
@Mod(ModInfo.MOD_ID)
class CardinalBoatsInit {

    init {

        TurnPriming.init()
        ManualSnap.init()
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory::class.java) {
            IConfigScreenFactory { _, p ->
                ConfigScreenBase(ConfigScreenSettings).apply {
                    parent = p
                    dumpWidgetTree()
                }
            }
        }
    }
}
