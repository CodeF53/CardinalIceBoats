package net.cardinalboats.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.entity.vehicle.BoatEntity;

import static net.cardinalboats.CardinalBoatsInit.LieAboutMovingForward;

@Mixin(value = KeyboardInput.class, priority = 1000)
public class ChatMoveLie {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z", ordinal = 0))
    private boolean lie(boolean original) {
        if (LieAboutMovingForward) {
            if (MinecraftClient.getInstance().currentScreen instanceof ChatScreen && MinecraftClient.getInstance().player.getVehicle() instanceof BoatEntity) {
                // lie about moving forward
                return true;
            } else {
                // chat isn't open, turn off lying
                LieAboutMovingForward = false;
            }
        }
        return original;
    }
}
