package net.cardinalboats.mixin;

import net.cardinalboats.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.cardinalboats.CardinalBoatsInit.LieAboutMovingForward;

@Mixin(value = Minecraft.class, priority = 1000)
public abstract class ChatMoveStartLying {
    @Shadow @Nullable public LocalPlayer player;
    @Shadow protected abstract void openChatScreen(String string);

    @Redirect(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openChatScreen(Ljava/lang/String;)V"))
    void moveChatBoi(Minecraft instance, String string) {
        // on opening the chat
        assert this.player != null;

        if (player.getVehicle() instanceof Boat && ModConfig.getInstance().moveWhileChatting) {
            // if the player is holding W
            if (Minecraft.getInstance().options.keyUp.isDown()) {
                // lie and tell the server that we are still moving forward despite having chat open
                LieAboutMovingForward = true;
            }
        }
        openChatScreen(string);
    }
}
