package net.cardinalboats

import net.cardinalboats.alias.AbstractBoatEntity
import net.cardinalboats.alias.MinecraftClient
import net.cardinalboats.config.CIBConfig
import net.cardinalboats.alias.KeyBinding
import net.cardinalboats.alias.InputUtilType
import net.cardinalboats.alias.InputUtil
import net.cardinalboats.alias.*


object ManualSnap: ManualSnapBase {

    private val logger = LogUtils.getLogger()

    override val manualSnapKey = KeyBinding("key.cardinalboats.snapManual",
                                            InputUtilType.KEYSYM,
                                            GLFW_KEY_UP,
                                            "category.cardinalboats.key_category_title")

    override val snap180 = KeyBinding("key.cardinalboats.snap180",
                                      InputUtilType.KEYSYM,
                                      GLFW_KEY_DOWN,
                                      "category.cardinalboats.key_category_title")


    @Suppress("EmptyWhileBlock", "MagicNumber")
    override fun tick(minecraft: MinecraftClient) {
        val player = minecraft.player
        if (player != null && player.vehicle != null && player.vehicle is AbstractBoatEntity) {
            val boat = player.vehicle as AbstractBoatEntity
            if (isIce(boat.steppingBlockState)) {
                while (manualSnapKey.wasPressed()) {
                    val snapAngle = if (CIBConfig.getInstance().eightWaySnapKey) 45 else 90
                    rotateBoat(boat, roundYRot(boat.yaw, snapAngle), true)
                }
                while (snap180.wasPressed()) {
                    rotateBoat(boat, boat.yaw % 360 - 180, CIBConfig.getInstance().maintainVelocityOnTurns)
                }
            }
        } else {
            while (manualSnapKey.wasPressed() || snap180.wasPressed()) {}
        }
    }
}
