package net.cardinalboats.config

import net.cardinalboats.generated.ModInfo
import net.minecraft.network.chat.Component
import org.anti_ad.mc.common.Savable
import org.anti_ad.mc.common.config.builder.ConfigDeclaration
import org.anti_ad.mc.common.config.builder.ConfigSaveLoadManager
import org.anti_ad.mc.common.config.builder.toMultiConfig
import org.anti_ad.mc.common.gui.screen.BaseConfigScreenSettings

object ConfigScreenSettings: BaseConfigScreenSettings() {


    private const val CONFIG_SCREEN_LABELS_PREFIX = "${ModInfo.MOD_ID}.gui.config."
    private const val CONFIG_SCREEN_OPTIONS_PREFIX = "${ModInfo.MOD_ID}.config."

    private const val FILE_NAME = "main-config.json"

    val configs = listOf(ModSettings)

    val saveLoadManager: ConfigSaveLoadManager = object : ConfigSaveLoadManager(ModInfo.MOD_ID, FILE_NAME, {configs.toMultiConfig()}) {
/*
        override fun save() {
            super.save()
        }
        override fun load() {
            super.load()
        }
*/
    }

    override val configScreenTitle: Component
        get() {
            return Component.translatable("${CONFIG_SCREEN_LABELS_PREFIX}title", ModInfo.MOD_VERSION)
        }

    override val saveManager: Savable = saveLoadManager

    override val configLabelsPrefix = CONFIG_SCREEN_LABELS_PREFIX

    override val configOptionsPrefix = CONFIG_SCREEN_OPTIONS_PREFIX

    override val openConfigHotkey =  null //ModSettings.OPEN_CONFIG_MENU

    override val configDeclarations: List<ConfigDeclaration>
        get() {
            val debug = false //ModSettings.DEBUG.value
            return if (debug) {
                configs
            } else {
                configs.filter {
                    true //todo add debug if needed
                }
            }
        }

}
