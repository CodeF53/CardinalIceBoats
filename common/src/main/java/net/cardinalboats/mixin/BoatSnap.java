package net.cardinalboats.mixin;

import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(BoatItem.class)
public class BoatSnap {
    private static final ArrayList<String> ice_types = new ArrayList<>(Arrays.asList("minecraft:ice", "minecraft:packed_ice", "minecraft:blue_ice", "minecraft:frosted_ice"));
    private static float oldRot;

    @Inject(method = "use", at = @At(value = "HEAD"))
    private void boatSnapSetAngle(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        // If we are putting a boat on a block
        HitResult lookingAt = player.pick(20.0D, 0.0F, false);
        if (lookingAt != null && lookingAt.getType() == HitResult.Type.BLOCK) {
            // If that block is ice, snap aim to nearest
            if (ice_types.contains(String.valueOf(Registry.BLOCK.getKey(level.getBlockState(((BlockHitResult) lookingAt).getBlockPos()).getBlock())))) {
                oldRot = player.getYRot();
                player.setYRot(Math.round(oldRot % 360/45.0)*45);
            }
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;setYRot(F)V", shift = At.Shift.AFTER))
    private void boatSnapResetAngle(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        player.setYRot(oldRot);
    }
}