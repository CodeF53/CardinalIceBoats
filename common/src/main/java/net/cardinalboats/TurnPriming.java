package net.cardinalboats;

import com.mojang.blaze3d.platform.InputConstants;

import net.cardinalboats.config.ModConfig;
import net.cardinalboats.optout.OptoutManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.HashMap;
import java.util.Map;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;

public class TurnPriming {
    public static final KeyMapping lQueueKey = new KeyMapping(
            "key.cardinalboats.prime_left",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_LEFT,
            "category.cardinalboats.key_category_title"
    );
    public static final KeyMapping rQueueKey = new KeyMapping(
            "key.cardinalboats.prime_right",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_RIGHT,
            "category.cardinalboats.key_category_title"
    );

    // Ran by forge and fabric initializer
    public static void init() {
        KeyMappingRegistry.register(lQueueKey);
        KeyMappingRegistry.register(rQueueKey);

        ClientTickEvent.CLIENT_POST.register(TurnPriming::tick);
    }

    private static boolean lTurnPrimed = false;
    private static boolean rTurnPrimed = false;

    public static void tick(Minecraft minecraft) {
        if (OptoutManager.Enabled) {
            if (minecraft.player != null && minecraft.player.isPassenger() && minecraft.player.getVehicle() instanceof Boat boat && Util.isIce(boat.getBlockStateOn())) {
                LocalPlayer player = minecraft.player;

                while (lQueueKey.consumeClick()) {
                    Util.ClientChatLog(minecraft.player, Component.translatable("info.cardinalboats.left_turn_queue").getString());
                    lTurnPrimed = true;
                    rTurnPrimed = false;
                }
                while (rQueueKey.consumeClick()) {
                    Util.ClientChatLog(minecraft.player, Component.translatable("info.cardinalboats.right_turn_queue").getString());
                    rTurnPrimed = true;
                    lTurnPrimed = false;
                }

                if (lTurnPrimed && shouldTurn(boat, minecraft.level, true)) {
                    Util.rotateBoat(boat, Util.roundYRot(boat.getYRot() - 90, 90), ModConfig.getInstance().maintainVelocityOnTurns);
                    lTurnPrimed = false;
                    Util.ClientChatLog(minecraft.player, Component.translatable("info.cardinalboats.left_turn_complete").getString());
                } else if (rTurnPrimed && shouldTurn(boat, minecraft.level, false)) {
                    Util.rotateBoat(boat, Util.roundYRot(boat.getYRot() + 90, 90), ModConfig.getInstance().maintainVelocityOnTurns);
                    rTurnPrimed = false;
                    Util.ClientChatLog(minecraft.player, Component.translatable("info.cardinalboats.right_turn_complete").getString());
                }
            } else {
                // if we aren't in the boat anymore, we don't care
                if (lTurnPrimed || rTurnPrimed) {
                    Util.ClientChatLog(minecraft.player, Component.translatable("info.cardinalboats.cancel").getString());
                }
                lTurnPrimed = false;
                rTurnPrimed = false;

                // not in a boat, don't care about any presses these buttons get right now
                while (lQueueKey.consumeClick()) {
                }
                while (rQueueKey.consumeClick()) {
                }
            }
        }
    }

    private static final Map<Direction, int[][]> toScanMapLeft = new HashMap<>() {{
        put(Direction.SOUTH, new int[][]{{ 3, 0}, { 3,-1}, { 3,-2}});
        put(Direction.NORTH, new int[][]{{-3, 0}, {-3, 1}, {-3, 2}});
        put(Direction.EAST,  new int[][]{{ 0,-3}, {-1,-3}, {-2,-3}});
        put(Direction.WEST,  new int[][]{{ 0, 3}, { 1, 3}, { 2, 3}});
    }};
    private static final Map<Direction, int[][]> toScanMapRight = new HashMap<>() {{
        put(Direction.SOUTH, new int[][]{{-3, 0}, {-3,-1}, {-3,-2}});
        put(Direction.NORTH, new int[][]{{ 3, 0}, { 3, 1}, { 3, 2}});
        put(Direction.EAST,  new int[][]{{ 0, 3}, {-1, 3}, {-2, 3}});
        put(Direction.WEST,  new int[][]{{ 0,-3}, { 1,-3}, { 2,-3}});
    }};
    private static final Map<Direction, int[][]> snapBlockMap = new HashMap<>() {{
        put(Direction.SOUTH, new int[][]{{ 0, 0}, { 0,-1}, { 0,-2}});
        put(Direction.NORTH, new int[][]{{ 0, 0}, { 0, 1}, { 0, 2}});
        put(Direction.EAST,  new int[][]{{ 0, 0}, {-1, 0}, {-2, 0}});
        put(Direction.WEST,  new int[][]{{ 0, 0}, { 1, 0}, { 2, 0}});
    }};

    public static boolean shouldTurn(Boat boat, ClientLevel level, boolean left) {
        int rootX = boat.getBlockX();
        int rootY = boat.getBlockY() - 1;
        int rootZ = boat.getBlockZ();

        // get the direction the boat is facing
        // north/south/east/west
        Direction direction = boat.getDirection();
        int[][] map;

        // get the block offsets for left/right
        if (left) {
            map = toScanMapLeft.get(direction);
        } else {
            map = toScanMapRight.get(direction);
        }

        for (int i = 0; i < map.length; i++) {
            // you gotta be moving at least
            if (i == 0 || boat.getDeltaMovement().length() > 5) {
                BlockPos testBlockPos = new BlockPos(rootX + map[i][0], rootY, rootZ + map[i][1]);
                if (Util.isIce(level.getBlockState(testBlockPos))) {
                    int[] snapBlock = snapBlockMap.get(direction)[i];
                    boat.setPos(rootX + snapBlock[0] + 0.5, boat.getY(), rootZ + snapBlock[1] + 0.5);
                    return true;
                }
            }
        }

        return false;
    }
}
