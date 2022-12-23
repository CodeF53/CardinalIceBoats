package net.cardinalboats;

import net.cardinalboats.TurnPriming;
import net.cardinalboats.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CardinalBoatsInit implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	public static boolean LieAboutMovingForward = false;

	@Override
	public void onInitializeClient() {
		TurnPriming.init();
		ManualSnap.init();
		ModConfig.init();
	}
}
