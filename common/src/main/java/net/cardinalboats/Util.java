package net.cardinalboats;

import net.cardinalboats.config.ModConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;

import static net.minecraft.util.Mth.DEG_TO_RAD;

public class Util {
    private static final ArrayList<String> ice_types = new ArrayList<>(Arrays.asList("minecraft:ice", "minecraft:packed_ice", "minecraft:blue_ice", "minecraft:frosted_ice"));

    public static void rotateBoat(Boat boat, Float rotation, Boolean maintainVelocity) {
        boat.setYRot(rotation);
        boat.deltaRotation = 0;
        boat.getControllingPassenger().setYRot(boat.getYRot());

        if (maintainVelocity) {
            // get current velocity vector length
            double currentVelocity = boat.getDeltaMovement().length();
            // create new vector normalized to rotation
            Vec3 newVelocity = new Vec3(0,0, currentVelocity).yRot(-rotation*DEG_TO_RAD); // Trig magic
            // give boat new thing
            boat.setDeltaMovement(newVelocity);
        } else {
            boat.setDeltaMovement(Vec3.ZERO);
        }
    }

    public static boolean isIce(BlockState blockState) {
        return ice_types.contains(Registry.BLOCK.getKey(blockState.getBlock()).toString());
    }

    public static void ClientChatLog(LocalPlayer player, String message) {
        if (ModConfig.getInstance().doChatShit) {
            player.displayClientMessage(Component.nullToEmpty("[cardinalboats] " + message), false);
        }
    }

    public static boolean shouldSnap(Level level, Player player) {
        // If we are putting a boat on a block
        HitResult lookingAt = player.pick(20.0D, 0.0F, false);
        if (lookingAt != null && lookingAt.getType() == HitResult.Type.BLOCK) {
            // If that block is ice, return true
            return ice_types.contains(String.valueOf(Registry.BLOCK.getKey(level.getBlockState(((BlockHitResult) lookingAt).getBlockPos()).getBlock())));
        }
        return false;
    }

    public static float roundYRot(float yRot, int toNearest) {
        return Math.round(yRot % 360 / toNearest) * toNearest;
    }
}
