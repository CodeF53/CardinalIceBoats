package net.cardinalboats.mixin;

import net.cardinalboats.config.CIBConfig;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.input.KeyboardInput;

import static net.cardinalboats.UtilKt.lieAboutMovingForward;


@Mixin(value = KeyboardInput.class)
public class ChatMoveLie {

    private int cardinalBoats$timeSinceChatClose = 0;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z", ordinal = 0))
    private boolean lie(boolean original) {
        if (lieAboutMovingForward) {
            if (MinecraftClient.getInstance().currentScreen instanceof ChatScreen && MinecraftClient.getInstance().player.getVehicle() instanceof AbstractBoatEntity) {
                cardinalBoats$timeSinceChatClose = 0;
                // lie about moving forward
                return true;
            } else {
                cardinalBoats$timeSinceChatClose++;
                if (cardinalBoats$timeSinceChatClose > CIBConfig.getInstance().ticksToMoveAfterChatHides) {
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
