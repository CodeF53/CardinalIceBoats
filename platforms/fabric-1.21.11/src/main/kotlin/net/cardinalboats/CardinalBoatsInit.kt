package net.cardinalboats

import net.cardinalboats.config.CIBConfig
import net.fabricmc.api.ClientModInitializer

class CardinalBoatsInit : ClientModInitializer {
    override fun onInitializeClient() {
        TurnPriming.init()
        ManualSnap.init()
        CIBConfig.init()
    }
}
