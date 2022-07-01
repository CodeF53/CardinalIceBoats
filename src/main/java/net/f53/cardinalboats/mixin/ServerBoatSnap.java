package net.f53.cardinalboats.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(BoatItem.class)
public class ServerBoatSnap {
	private static final ArrayList<String> ice_types = new ArrayList<>(Arrays.asList("minecraft:ice", "minecraft:packed_ice", "minecraft:blue_ice", "minecraft:frosted_ice"));

	@ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;setYRot(F)V"))
	private float serverBoatSnap(float par1) {
		HitResult lookingAt = Minecraft.getInstance().getCameraEntity().pick(20.0D, 0.0F, false);
		if (lookingAt != null && lookingAt.getType() == HitResult.Type.BLOCK) {
			// Gets key of targeted block in an absolutely disgusting way (BlockHit -> BlockState -> Block -> Key -> String)
			String lookingAtBlockKey = String.valueOf(Registry.BLOCK.getKey(Minecraft.getInstance().level.getBlockState(((BlockHitResult) lookingAt).getBlockPos()).getBlock()));
			if (ice_types.contains(lookingAtBlockKey)) {
				// round angle to nearest 45 degrees
				return Math.round(par1 % 360/45.0)*45;
			}
		}
		return par1;
	}
}