package net.cardinalboats.optout;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.networking.NetworkManager;
import net.cardinalboats.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class OptoutManager {
    public static final ResourceLocation OPTOUT_PACKET = new ResourceLocation("cardinalboats", "optout");

    public static boolean Enabled = true;

    public static void init() {
        // Ran on connecting to a server that has opted out
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, OPTOUT_PACKET, (buf, context) -> {
            Enabled = false;
            Util.ClientChatLog(Minecraft.getInstance().player, "Server has opted out of Turn Priming, so it wont work");
        });

        // Ran when disconnecting from all servers
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((@Nullable LocalPlayer player) -> Enabled = true);
    }
}
