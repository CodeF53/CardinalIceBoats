package net.cardinalboats.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
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

import static net.cardinalboats.Util.roundYRot;
import static net.cardinalboats.Util.shouldSnap;

@Mixin(BoatItem.class)
public class ClientBoatSnap {
    @Inject(method = "use", at = @At(value = "HEAD"))
    private void clientBoatSnap(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (shouldSnap(level, player)) {
            Minecraft.getInstance().player.connection.send(new ServerboundMovePlayerPacket.Rot(roundYRot(player.getYRot()), player.getXRot(), player.isOnGround()));
        }
    }
}