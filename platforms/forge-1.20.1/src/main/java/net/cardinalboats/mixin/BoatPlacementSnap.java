package net.cardinalboats.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


import net.minecraft.world.item.BoatItem;

import static net.cardinalboats.UtilKt.roundYRot;
import static net.cardinalboats.UtilKt.shouldSnap;

@Mixin(BoatItem.class)
public class BoatPlacementSnap {
    @ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getYRot()F"))
    private float boatSnap(float original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) Level world) {
        if (shouldSnap(world, player))
            return roundYRot(player.getYRot(), 45);
        return original;
    }
}
