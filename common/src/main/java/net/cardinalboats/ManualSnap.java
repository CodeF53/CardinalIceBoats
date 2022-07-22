package net.cardinalboats;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.cardinalboats.config.ModConfig;
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

    public static void init() {
        KeyMappingRegistry.register(manualSnapKey);

        ClientTickEvent.CLIENT_POST.register(ManualSnap::tick);
    }

    public static void tick(Minecraft minecraft) {
        if (minecraft.player != null && minecraft.player.isPassenger() && minecraft.player.getVehicle() instanceof Boat boat) {
            while (manualSnapKey.consumeClick()) {
                Util.rotateBoat(boat, Util.roundYRot(boat.getYRot(), ModConfig.getInstance().eightWaySnapKey? 45:90), true);
            }
        } else {
            while (manualSnapKey.consumeClick()) {}
        }
    }
}
