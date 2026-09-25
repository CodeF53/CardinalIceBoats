package net.cardinalboats

import com.mojang.blaze3d.platform.InputConstants
import net.cardinalboats.alias.KEY_BINDING_CATEGORY
import net.cardinalboats.config.ModSettings
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.vehicle.boat.AbstractBoat

object ManualSnap: ManualSnapBase {

    override val manualSnapKey = KeyMapping("key.cardinalboats.snapManual",
                                            InputConstants.Type.KEYSYM,
                                            InputConstants.KEY_UP,
                                            KEY_BINDING_CATEGORY)

    override val snap180 = KeyMapping("key.cardinalboats.snap180",
                                      InputConstants.Type.KEYSYM,
                                      InputConstants.KEY_DOWN,
                                      KEY_BINDING_CATEGORY)



    @Suppress("EmptyWhileBlock", "MagicNumber")
    override fun tick(minecraft: Minecraft) {
        val player = minecraft.player
        if (player?.vehicle != null && player.vehicle is AbstractBoat) {
            val boat = player.vehicle as AbstractBoat
            if (isIce(boat.blockStateOn)) {
                while (manualSnapKey.consumeClick()) {
                    val snapAngle = if (ModSettings.EIGHT_WAY_SNAP_KEY.value.toBoolean()) 45 else 90
                    rotateBoat(boat, roundYRot(boat.yRot, snapAngle), true)
                }
                while (snap180.consumeClick()) {
                    rotateBoat(boat, boat.yRot % 360 - 180, ModSettings.MAINTAIN_VELOCITY_ON_TURNS.value)
                }
            }
        } else {
            while (manualSnapKey.consumeClick() || snap180.consumeClick()) {}
        }
    }
}
