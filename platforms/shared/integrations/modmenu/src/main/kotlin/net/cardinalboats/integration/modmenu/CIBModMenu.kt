package net.cardinalboats.integration.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import net.cardinalboats.config.CIBConfig

class CIBModMenu: ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*>? {
        return ConfigScreenFactory {
            AutoConfig.getConfigScreen(CIBConfig::class.java, it).get()
        }
    }

}
