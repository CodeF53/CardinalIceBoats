package net.cardinalboats;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BoatSnapUtil {
    private static final ArrayList<String> ice_types = new ArrayList<>(Arrays.asList("minecraft:ice", "minecraft:packed_ice", "minecraft:blue_ice", "minecraft:frosted_ice"));

    public static boolean shouldSnap(Level level, Player player) {
        // If we are putting a boat on a block
        HitResult lookingAt = player.pick(20.0D, 0.0F, false);
        if (lookingAt != null && lookingAt.getType() == HitResult.Type.BLOCK) {
            // If that block is ice, return true
            return ice_types.contains(String.valueOf(Registry.BLOCK.getKey(level.getBlockState(((BlockHitResult) lookingAt).getBlockPos()).getBlock())));
        }
        return false;
    }

    public static float roundYRot(float yRot) {
        return Math.round(yRot % 360/45.0)*45;
    }
}
