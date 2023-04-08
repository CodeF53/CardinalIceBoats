package net.cardinalboats;

import net.cardinalboats.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.vehicle.BoatEntity;

public class ManualSnap {
    public static final KeyBinding manualSnapKey = new KeyBinding(
            "key.cardinalboats.snapManual",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_UP,
            "category.cardinalboats.key_category_title"
    );

    // Run by fabric initializer
    public static void init() {
        KeyBindingHelper.registerKeyBinding(manualSnapKey);

        ClientTickEvents.END_CLIENT_TICK.register(ManualSnap::tick);
    }

    public static void tick(MinecraftClient minecraft) {
        if (minecraft.player != null && minecraft.player.hasVehicle() && minecraft.player.getVehicle() instanceof BoatEntity boat && Util.isIce(boat.getSteppingBlockState())) {
            while (manualSnapKey.wasPressed()) {
                Util.rotateBoat(boat, Util.roundYRot(boat.getYaw(), ModConfig.getInstance().eightWaySnapKey? 45:90), true);
            }
        } else {
            while (manualSnapKey.wasPressed()) {}
        }
    }
}
