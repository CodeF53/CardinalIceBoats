package net.cardinalboats;

import net.cardinalboats.config.ModConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.Arrays;

public class Util {
    private static final ArrayList<String> ice_types = new ArrayList<>(Arrays.asList("minecraft:ice", "minecraft:packed_ice", "minecraft:blue_ice", "minecraft:frosted_ice"));

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

    public static float roundYRot(float yRot) {
        return Math.round(yRot % 360/45.0)*45;
    }
}
