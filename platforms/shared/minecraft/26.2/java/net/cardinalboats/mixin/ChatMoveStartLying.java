package net.cardinalboats.mixin;

import net.cardinalboats.config.ModSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static net.cardinalboats.UtilKt.lieAboutMovingForward;

@Mixin(value = Gui.class, priority = 1000)
public abstract class ChatMoveStartLying {
    @Final
    @Shadow
    @Nullable
    private Minecraft minecraft;

    @Inject(method = "openChatScreen", at = @At("HEAD"))
    void moveChatBoi(CallbackInfo ci) {
        // on opening the chat
        var player = this.minecraft != null ? this.minecraft.player : null;
        if  (player != null) {
            if (player.getVehicle() instanceof AbstractBoat && ModSettings.INSTANCE.getMOVE_WHILE_CHATTING().getValue()) {
                // if the player is holding W
                if (Minecraft.getInstance().options.keyUp.isDown()) {
                    // lie and tell the server that we are still moving forward despite having chat open
                    lieAboutMovingForward = true;
                }
            }
        }
    }
}
