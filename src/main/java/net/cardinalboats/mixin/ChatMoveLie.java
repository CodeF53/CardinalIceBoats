package net.cardinalboats.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.cardinalboats.CardinalBoatsInit.LieAboutMovingForward;

@Mixin(value = KeyboardInput.class, priority = 1000)
public class ChatMoveLie {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z", ordinal = 0))
    private boolean lie(KeyMapping instance) {
        if (LieAboutMovingForward) {
            if (Minecraft.getInstance().screen instanceof ChatScreen && Minecraft.getInstance().player.getVehicle() instanceof Boat) {
                // lie about moving forward
                return true;
            } else {
                // chat isn't open, turn off lying
                LieAboutMovingForward = false;
            }
        }
        return instance.isDown();
    }
}
