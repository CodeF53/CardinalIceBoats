package net.cardinalboats

import com.mojang.blaze3d.platform.InputConstants
import net.cardinalboats.alias.KEY_BINDING_CATEGORY
import net.cardinalboats.config.CIBConfig
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
        if (player != null && player.vehicle != null && player.vehicle is AbstractBoat) {
            val boat = player.vehicle as AbstractBoat
            if (isIce(boat.blockStateOn)) {
                while (manualSnapKey.consumeClick()) {
                    val snapAngle = if (CIBConfig.getInstance().eightWaySnapKey) 45 else 90
                    rotateBoat(boat, roundYRot(boat.yRot, snapAngle), true)
                }
                while (snap180.consumeClick()) {
                    rotateBoat(boat, boat.yRot % 360 - 180, CIBConfig.getInstance().maintainVelocityOnTurns)
                }
            }
        } else {
            while (manualSnapKey.consumeClick() || snap180.consumeClick()) {}
        }
    }
}
