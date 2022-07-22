package net.cardinalboats.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;


import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = "CardinalBoat")
public class ModConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean doChatShit = true;

    @ConfigEntry.Gui.Tooltip
    public boolean maintainVelocityOnTurns = false;

    @ConfigEntry.Gui.Tooltip
    public boolean eightWaySnapKey = true;

    public static void init() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }

    public static ModConfig getInstance() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
