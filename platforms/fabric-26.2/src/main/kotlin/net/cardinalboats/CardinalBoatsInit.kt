package net.cardinalboats

import net.fabricmc.api.ClientModInitializer
import org.anti_ad.mc.libipn.config.ConfigScreenSettings

class CardinalBoatsInit : ClientModInitializer {
    override fun onInitializeClient() {
        TurnPriming.init()
        ManualSnap.init()
        ConfigScreenSettings.initMainConfig()
    }
}
