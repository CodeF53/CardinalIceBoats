package net.cardinalboats.integration.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfigClient
import net.cardinalboats.config.CIBConfig

class CIBModMenu: ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*>? {
        return ConfigScreenFactory {
            AutoConfigClient.getConfigScreen(CIBConfig::class.java, it).get()
        }
    }

}
