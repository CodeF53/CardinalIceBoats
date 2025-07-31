package net.cardinalboats.mixin;

import net.cardinalboats.config.CIBConfig;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static net.cardinalboats.UtilKt.lieAboutMovingForward;

@Mixin(value = Minecraft.class, priority = 1000)
public abstract class ChatMoveStartLying {
    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openChatScreen(Ljava/lang/String;)V"))
    void moveChatBoi(CallbackInfo ci) {
        // on opening the chat
        assert this.player != null;

        if (player.getVehicle() instanceof Boat && CIBConfig.getInstance().moveWhileChatting) {
            // if the player is holding W
            if (Minecraft.getInstance().options.keyUp.isDown()) {
                // lie and tell the server that we are still moving forward despite having chat open
                lieAboutMovingForward = true;
            }
        }
    }
}
