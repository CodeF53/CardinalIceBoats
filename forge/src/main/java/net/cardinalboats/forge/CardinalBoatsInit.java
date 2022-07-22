package net.cardinalboats.forge;

import me.shedaniel.autoconfig.AutoConfig;
import net.cardinalboats.ManualSnap;
import net.cardinalboats.TurnPriming;
import net.cardinalboats.config.ModConfig;
import net.cardinalboats.optout.OptoutManager;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("cardinalboats")
public class CardinalBoatsInit {
    public CardinalBoatsInit() {
        TurnPriming.init();
        ManualSnap.init();
        ModConfig.init();
        OptoutManager.init();

        // bind the Config button in the forge mod menu to our config screen
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) ->
                        AutoConfig.getConfigScreen(ModConfig.class, parent).get()));
    }
}
