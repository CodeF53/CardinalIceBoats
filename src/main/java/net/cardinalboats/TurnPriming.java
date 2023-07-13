package net.cardinalboats;

import net.cardinalboats.config.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.AirBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static net.minecraft.util.math.Direction.NORTH;
import static net.minecraft.util.math.Direction.SOUTH;
import static net.minecraft.util.math.Direction.EAST;
import static net.minecraft.util.math.Direction.WEST;

public class TurnPriming {
    public static final KeyBinding lQueueKey = new KeyBinding(
            "key.cardinalboats.prime_left",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_LEFT,
            "category.cardinalboats.key_category_title"
    );
    public static final KeyBinding rQueueKey = new KeyBinding(
            "key.cardinalboats.prime_right",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_RIGHT,
            "category.cardinalboats.key_category_title"
    );

    public static final KeyBinding smartCenterKey = new KeyBinding(
        "key.cardinalboats.smartCenter",
        InputUtil.Type.KEYSYM,
        InputUtil.GLFW_KEY_BACKSLASH,
        "category.cardinalboats.key_category_title"
    );

    // Run by fabric initializer
    public static void init() {
        KeyBindingHelper.registerKeyBinding(lQueueKey);
        KeyBindingHelper.registerKeyBinding(rQueueKey);
        KeyBindingHelper.registerKeyBinding(smartCenterKey);

        ClientTickEvents.END_CLIENT_TICK.register(TurnPriming::tick);
    }

    private static boolean lTurnPrimed = false;
    private static boolean rTurnPrimed = false;

    public static void tick(MinecraftClient minecraft) {
        if (minecraft.player != null && minecraft.player.hasVehicle() && minecraft.player.getVehicle() instanceof BoatEntity boat) {
            if (Util.isIce(boat.getSteppingBlockState())) {
                ClientPlayerEntity player = minecraft.player;

                while (lQueueKey.wasPressed()) {
                    Util.ClientChatLog(player, Text.translatable("info.cardinalboats.left_turn_queue").getString());
                    lTurnPrimed = true;
                    rTurnPrimed = false;
                }
                while (rQueueKey.wasPressed()) {
                    Util.ClientChatLog(player, Text.translatable("info.cardinalboats.right_turn_queue").getString());
                    rTurnPrimed = true;
                    lTurnPrimed = false;
                }

                if (ModConfig.getInstance().alwaysSmartCenter && boat.getYaw() % 90 == 0) { smartCenter(boat); }
                while (smartCenterKey.wasPressed()) { smartCenter(boat); }

                if (lTurnPrimed && shouldTurn(boat, minecraft.world, true)) {
                    Util.rotateBoat(boat, Util.roundYRot(boat.getYaw() - 90, 90), ModConfig.getInstance().maintainVelocityOnTurns);
                    lTurnPrimed = false;
                    Util.ClientChatLog(player, Text.translatable("info.cardinalboats.left_turn_complete").getString());
                    if (ModConfig.getInstance().smartCenterPrimedTurn) smartCenter(boat);
                } else if (rTurnPrimed && shouldTurn(boat, minecraft.world, false)) {
                    Util.rotateBoat(boat, Util.roundYRot(boat.getYaw() + 90, 90), ModConfig.getInstance().maintainVelocityOnTurns);
                    rTurnPrimed = false;
                    Util.ClientChatLog(player, Text.translatable("info.cardinalboats.right_turn_complete").getString());
                    if (ModConfig.getInstance().smartCenterPrimedTurn) smartCenter(boat);
                }
            } else {
                while (lQueueKey.wasPressed() || rQueueKey.wasPressed() || smartCenterKey.wasPressed()) {}
            }
        } else {
            // if we aren't in the boat anymore, we don't care
            if (lTurnPrimed || rTurnPrimed) {
                Util.ClientChatLog(minecraft.player, Text.translatable("info.cardinalboats.cancel").getString());
            }
            lTurnPrimed = false;
            rTurnPrimed = false;

            // not in a boat, don't care about any presses these buttons get right now
            while (lQueueKey.wasPressed() || rQueueKey.wasPressed() || smartCenterKey.wasPressed()) {}
        }
    }

    private static final Map<Direction, int[][]> toScanMapLeft = new HashMap<>() {{
        put(SOUTH, new int[][]{{ 3, 0}, { 3,-1}, { 3,-2}});
        put(NORTH, new int[][]{{-3, 0}, {-3, 1}, {-3, 2}});
        put(EAST,  new int[][]{{ 0,-3}, {-1,-3}, {-2,-3}});
        put(WEST,  new int[][]{{ 0, 3}, { 1, 3}, { 2, 3}});
    }};
    private static final Map<Direction, int[][]> toScanMapRight = new HashMap<>() {{
        put(SOUTH, new int[][]{{-3, 0}, {-3,-1}, {-3,-2}});
        put(NORTH, new int[][]{{ 3, 0}, { 3, 1}, { 3, 2}});
        put(EAST,  new int[][]{{ 0, 3}, {-1, 3}, {-2, 3}});
        put(WEST,  new int[][]{{ 0,-3}, { 1,-3}, { 2,-3}});
    }};
    private static final Map<Direction, int[][]> snapBlockMap = new HashMap<>() {{
        put(SOUTH, new int[][]{{ 0, 0}, { 0,-1}, { 0,-2}});
        put(NORTH, new int[][]{{ 0, 0}, { 0, 1}, { 0, 2}});
        put(EAST,  new int[][]{{ 0, 0}, {-1, 0}, {-2, 0}});
        put(WEST,  new int[][]{{ 0, 0}, { 1, 0}, { 2, 0}});
    }};

    public static boolean shouldTurn(BoatEntity boat, ClientWorld level, boolean left) {
        int rootX = boat.getBlockX();
        int rootY = boat.getBlockY() - 1;
        int rootZ = boat.getBlockZ();

        // get the direction the boat is facing
        // north/south/east/west
        Direction direction = boat.getHorizontalFacing();
        int[][] map;

        // get the block offsets for left/right
        if (left) {
            map = toScanMapLeft.get(direction);
        } else {
            map = toScanMapRight.get(direction);
        }
        for (int i = 0; i < map.length; i++) {
            BlockPos testBlockPos = new BlockPos(rootX + map[i][0], rootY, rootZ + map[i][1]);
            if (Util.isIce(level.getBlockState(testBlockPos))) {
                int[] snapBlock = snapBlockMap.get(direction)[i];
                boat.setPosition(rootX + snapBlock[0] + 0.5, boat.getY(), rootZ + snapBlock[1] + 0.5);
                return true;
            }
        }

        return false;
    }

    public static void smartCenter(BoatEntity boat) {
        World world = boat.getWorld();
        Direction direction = boat.getHorizontalFacing();
        int rootX = boat.getBlockX();
        int rootY = boat.getBlockY();
        int rootZ = boat.getBlockZ();

        int scanAhead = ModConfig.getInstance().smartCenterLookAhead;
        if (direction == NORTH || direction == SOUTH) {
            int startZ = direction == NORTH ? -scanAhead : -1;
            int endZ = direction == NORTH ? 1 : scanAhead;
            double nudgeX = calculateNudge(world, startZ, endZ, z -> new BlockPos(rootX - 1, rootY, rootZ + z), z -> new BlockPos(rootX + 1, rootY, rootZ + z));
            boat.setPosition(rootX + 0.5 + nudgeX, boat.getY(), boat.getZ());
        } else {
            int startX = direction == WEST ? -scanAhead : -1;
            int endX = direction == WEST ? 1 : scanAhead;
            double nudgeZ = calculateNudge(world, startX, endX, x -> new BlockPos(rootX + x, rootY, rootZ - 1), x -> new BlockPos(rootX + x, rootY, rootZ + 1));
            boat.setPosition(boat.getX(), boat.getY(), rootZ + 0.5 + nudgeZ);
        }
    }

    private static double calculateNudge(World world, int start, int end, Function<Integer, BlockPos> leftBlockPosFunc, Function<Integer, BlockPos> rightBlockPosFunc) {
        int nudge = 0;
        for (int i = start; i <= end; i++) {
            BlockPos leftBlockPos = leftBlockPosFunc.apply(i);
            BlockPos rightBlockPos = rightBlockPosFunc.apply(i);
            if (!(world.getBlockState(leftBlockPos).getBlock() instanceof AirBlock))
                nudge += 1;
            if (!(world.getBlockState(rightBlockPos).getBlock() instanceof AirBlock))
                nudge -= 1;
        }
        return MathHelper.clamp(nudge, -0.2, 0.2);
    }
}
