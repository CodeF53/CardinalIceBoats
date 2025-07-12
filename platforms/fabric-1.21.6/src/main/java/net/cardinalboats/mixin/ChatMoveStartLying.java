package net.cardinalboats.mixin;

import net.cardinalboats.config.CIBConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static net.cardinalboats.UtilKt.lieAboutMovingForward;

@Mixin(value = MinecraftClient.class, priority = 1000)
public abstract class ChatMoveStartLying {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;openChatScreen(Ljava/lang/String;)V"))
    void moveChatBoi(CallbackInfo ci) {
        // on opening the chat
        if (this.player != null) {
            if (player.getVehicle() instanceof AbstractBoatEntity && CIBConfig.getInstance().moveWhileChatting) {
                // if the player is holding W
                if (MinecraftClient.getInstance().options.forwardKey.isPressed()) {
                    // lie and tell the server that we are still moving forward despite having chat open
                    lieAboutMovingForward = true;
                }
            }
        }
    }
}
