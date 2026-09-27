package net.cardinalboats

import net.cardinalboats.alias.KEY_BINDING_CATEGORY_REG
import net.cardinalboats.generated.ModInfo
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier
import org.anti_ad.mc.libipn.config.ConfigScreenSettings
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class CardinalBoatsInit : ClientModInitializer {
    @OptIn(ExperimentalAtomicApi::class)
    override fun onInitializeClient() {
        KEY_BINDING_CATEGORY_REG.compareAndSet(null,
                                               KeyMapping.Category.register(Identifier.fromNamespaceAndPath(ModInfo.MOD_ID,
                                                                                                            "binding_category")))
        TurnPriming.init()
        ManualSnap.init()
        ConfigScreenSettings.initMainConfig()
    }
}
