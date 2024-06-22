package net.cardinalboats.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.cardinalboats.Util.roundYRot;
import static net.cardinalboats.Util.shouldSnap;

import net.minecraft.item.BoatItem;

@Mixin(BoatItem.class)
public class BoatPlacementSnap {
    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getYaw()F"))
    private float boatSnap(float original, @Local(argsOnly = true) PlayerEntity player, @Local(argsOnly = true) World world) {
        if (shouldSnap(world, player))
            return roundYRot(player.getYaw(), 45);
        return original;
    }
}
