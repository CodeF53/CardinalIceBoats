package net.cardinalboats

import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft

internal var keyMappings: Array<KeyMapping>
    get() = Minecraft.getInstance().options.keyMappings;
    set(value) {
        Minecraft.getInstance().options.keyMappings = value;
    }
