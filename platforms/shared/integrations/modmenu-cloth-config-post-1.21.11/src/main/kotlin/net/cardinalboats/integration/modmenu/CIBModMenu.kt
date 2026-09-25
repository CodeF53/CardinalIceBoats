package net.cardinalboats.integration.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.cardinalboats.config.ConfigScreenSettings
import org.anti_ad.mc.common.gui.screen.ConfigScreenBase

class CIBModMenu: ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { p ->
            ConfigScreenBase(ConfigScreenSettings).apply {
                parent = p
                dumpWidgetTree()
            }
        }
    }

}
