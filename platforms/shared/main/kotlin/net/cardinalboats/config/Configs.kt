@file:Suppress("unused")
package net.cardinalboats.config


import net.cardinalboats.generated.ModInfo
import org.anti_ad.mc.common.config.builder.CATEGORY
import org.anti_ad.mc.common.config.builder.*
import org.anti_ad.mc.common.input.KeybindSettings
import org.anti_ad.mc.common.vanilla.alias.glue.I18n

const val CONFIG_CATEGORY = "${ModInfo.MOD_ID}.config.category"

private const val ENUM = "${ModInfo.MOD_ID}.enum"

enum class SnapKeyMode {
    EIGHT_WAY,
    FOUR_WAY;

    override fun toString(): String =
            I18n.translate("$ENUM.snap_key_mode.${name.lowercase()}")

    fun toBoolean(): Boolean = this == EIGHT_WAY
}


object ModSettings : ConfigDeclaration {

    override val builder = createBuilder()

        .CATEGORY("§§vgap:3")
//        .CATEGORY("$CONFIG_CATEGORY.general_hotkeys")
//    val OPEN_CONFIG_MENU                      /**/ by hotkey("C,B",KeybindSettings.INGAME_DEFAULT)

        .CATEGORY("$CONFIG_CATEGORY.boat")
    val DO_CHAT_SHIFT                         /**/ by bool(true)
    val MAINTAIN_VELOCITY_ON_TURNS            /**/ by bool(false)
    val EIGHT_WAY_SNAP_KEY                    /**/ by enum(SnapKeyMode.EIGHT_WAY)
    val MOVE_WHILE_CHATTING                   /**/ by bool(true)
    val TICKS_TO_MOVE_AFTER_CHAT_HIDES        /**/ by int(15, 0, 40)
    val ALWAYS_SMART_CENTER                   /**/ by bool(false)
    val SMART_CENTER_LOOK_AHEAD               /**/ by int(5, 1, 10)
    val SMART_CENTER_PRIMED_TURN              /**/ by bool(true)
    val SMART_CENTER_PRIMED_TURN_DELAY_TICKS  /**/ by int(30, 1, 50)

//        .CATEGORY("§§hide")
//        .CATEGORY("$CONFIG_CATEGORY.privacy")
//    val ENABLE_UPDATES_CHECK                  /**/ by bool(true)

//        .CATEGORY("$CONFIG_CATEGORY.debug")
//    val DEBUG                                 /**/ by bool(false)

}
