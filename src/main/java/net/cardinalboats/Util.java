package net.cardinalboats;

import net.cardinalboats.config.ModConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.regex.Pattern;

import static net.minecraft.util.math.MathHelper.RADIANS_PER_DEGREE;

public class Util {
    private static final Pattern icePattern = Pattern.compile("(\\b|_)ice\\b", Pattern.CASE_INSENSITIVE);

    public static void rotateBoat(BoatEntity boat, Float rotation, Boolean maintainVelocity) {
        boat.setYaw(rotation);
        boat.yawVelocity = 0;
        boat.getControllingPassenger().setYaw(boat.getYaw());

        if (maintainVelocity) {
            // get current velocity vector length
            double currentVelocity = boat.getVelocity().length();
            // create new vector normalized to rotation
            Vec3d newVelocity = new Vec3d(0,0, currentVelocity).rotateY(-rotation*RADIANS_PER_DEGREE); // Trig magic
            // give boat new thing
            boat.setVelocity(newVelocity);
        } else {
            boat.setVelocity(Vec3d.ZERO);
        }
    }

    public static boolean isIce(BlockState blockState) {
        return icePattern.matcher(blockState.getBlock().toString()).find();
    }

    public static void ClientChatLog(ClientPlayerEntity player, String message) {
        if (player == null) return;

        if (ModConfig.getInstance().doChatShit) {
            player.sendMessage(Text.of("[cardinalboats] " + message), false);
        }
    }

    public static boolean shouldSnap(World level, PlayerEntity player) {
        // If we are putting a boat on a block
        HitResult lookingAt = player.raycast(20.0D, 0.0F, false);
        if (lookingAt != null && lookingAt.getType() == HitResult.Type.BLOCK) {
            // If that block is ice, return true
            return isIce(level.getBlockState(((BlockHitResult) lookingAt).getBlockPos()));
        }
        return false;
    }

    public static float roundYRot(float yRot, int toNearest) {
        return Math.round(yRot % 360 / toNearest) * toNearest;
    }
}
