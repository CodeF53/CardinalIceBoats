package net.cardinalboats.mixin;


import net.cardinalboats.config.ModSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.anti_ad.mc.common.vanilla.Vanilla;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;


import static net.cardinalboats.UtilKt.lieAboutMovingForward;


@Mixin(value = KeyboardInput.class, priority = 1000)
public class ChatMoveLie {

    @Unique
    private int cardinalBoats$timeSinceChatClose = 0;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z", ordinal = 0))
    private boolean lie(boolean original) {
        if (Minecraft.getInstance().player == null) return original;
        if (lieAboutMovingForward) {
            if (Vanilla.INSTANCE.screen() instanceof ChatScreen && Minecraft.getInstance().player.getVehicle() instanceof AbstractBoat) {
                // lie about moving forward
                return true;
            } else {
                cardinalBoats$timeSinceChatClose++;
                if (cardinalBoats$timeSinceChatClose > ModSettings.INSTANCE.getTICKS_TO_MOVE_AFTER_CHAT_HIDES().getValue()) {
                    // chat isn't open, turn off lying
                    lieAboutMovingForward = false;
                } else {
                    return true;
                }
            }
        }
        return original;
    }
}
