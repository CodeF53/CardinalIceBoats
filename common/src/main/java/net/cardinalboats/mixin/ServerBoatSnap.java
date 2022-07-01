package net.cardinalboats.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.cardinalboats.BoatSnapUtil.roundYRot;
import static net.cardinalboats.BoatSnapUtil.shouldSnap;

@Mixin(BoatItem.class)
public class ServerBoatSnap {
    @Inject(method = "use", at = @At(value = "HEAD"))
    private void serverBoatSnap(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (shouldSnap(level, player)) {
            player.setYRot(roundYRot(player.getYRot()));
        }
    }
}