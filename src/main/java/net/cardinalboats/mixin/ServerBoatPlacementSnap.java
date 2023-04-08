package net.cardinalboats.mixin;

import net.cardinalboats.Util;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoatItem.class)
public class ServerBoatPlacementSnap {
    @Inject(method = "use", at = @At(value = "HEAD"))
    private void serverBoatSnap(World level, PlayerEntity player, Hand interactionHand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (Util.shouldSnap(level, player)) {
            player.setYaw(Util.roundYRot(player.getYaw(), 45));
        }
    }
}