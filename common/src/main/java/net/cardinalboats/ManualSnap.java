package net.cardinalboats;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;

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
                boat.setYRot(Util.roundYRot(boat.getYRot()));
                boat.setDeltaMovement(Vec3.ZERO);
                boat.deltaRotation = 0;
                minecraft.player.setYRot(boat.getYRot());
            }
        } else {
            while (manualSnapKey.consumeClick()) {}
        }
    }
}
