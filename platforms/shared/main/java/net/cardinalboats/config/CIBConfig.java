package net.cardinalboats.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;


import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

@Config(name = "CardinalBoat")
public class CIBConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean doChatShit = true;

    @ConfigEntry.Gui.Tooltip
    public boolean maintainVelocityOnTurns = false;

    @ConfigEntry.Gui.Tooltip
    public boolean eightWaySnapKey = true;

    @ConfigEntry.Gui.Tooltip
    public boolean moveWhileChatting = true;

    @ConfigEntry.Gui.Tooltip
    public boolean alwaysSmartCenter = false;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
    public int smartCenterLookAhead = 5;

    @ConfigEntry.Gui.Tooltip
    public boolean smartCenterPrimedTurn = true;

    public static void init() {
        AutoConfig.register(CIBConfig.class, Toml4jConfigSerializer::new);
    }

    public static CIBConfig getInstance() {
        return AutoConfig.getConfigHolder(CIBConfig.class).getConfig();
    }
}
