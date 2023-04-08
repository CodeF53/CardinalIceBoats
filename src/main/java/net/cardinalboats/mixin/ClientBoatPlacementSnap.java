package net.cardinalboats.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.cardinalboats.Util.roundYRot;
import static net.cardinalboats.Util.shouldSnap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(BoatItem.class)
public class ClientBoatPlacementSnap {
    @Inject(method = "use", at = @At(value = "HEAD"))
    private void clientBoatSnap(World level, PlayerEntity player, Hand interactionHand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (shouldSnap(level, player)) {
            MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(roundYRot(player.getYaw(), 45), player.getPitch(), player.isOnGround()));
        }
    }
}