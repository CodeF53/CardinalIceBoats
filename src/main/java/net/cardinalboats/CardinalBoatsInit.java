package net.cardinalboats;

import net.cardinalboats.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;

public class CardinalBoatsInit implements ClientModInitializer {
	public static boolean LieAboutMovingForward = false;

	@Override
	public void onInitializeClient() {
		TurnPriming.init();
		ManualSnap.init();
		ModConfig.init();
	}
}
