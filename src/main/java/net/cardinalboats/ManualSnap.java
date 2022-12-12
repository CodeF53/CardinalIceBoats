package net.cardinalboats;

import com.mojang.blaze3d.platform.InputConstants;
import net.cardinalboats.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.vehicle.Boat;

public class ManualSnap {
    public static final KeyMapping manualSnapKey = new KeyMapping(
            "key.cardinalboats.snapManual",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_UP,
            "category.cardinalboats.key_category_title"
    );

    // Run by fabric initializer
    public static void init() {
        KeyBindingHelper.registerKeyBinding(manualSnapKey);

        ClientTickEvents.END_CLIENT_TICK.register(ManualSnap::tick);
    }

    public static void tick(Minecraft minecraft) {
        if (minecraft.player != null && minecraft.player.isPassenger() && minecraft.player.getVehicle() instanceof Boat boat && Util.isIce(boat.getBlockStateOn())) {
            while (manualSnapKey.consumeClick()) {
                Util.rotateBoat(boat, Util.roundYRot(boat.getYRot(), ModConfig.getInstance().eightWaySnapKey? 45:90), true);
            }
        } else {
            while (manualSnapKey.consumeClick()) {}
        }
    }
}
