package net.cardinalboats.forge;

import net.cardinalboats.TurnPriming;
import net.cardinalboats.config.ModConfig;
import net.minecraftforge.fml.common.Mod;

@Mod("cardinalboats")
public class CardinalBoatsInit {
    public CardinalBoatsInit() {
        TurnPriming.init();
        ModConfig.init();
    }
}
