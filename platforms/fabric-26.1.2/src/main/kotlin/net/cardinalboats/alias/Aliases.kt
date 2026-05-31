package net.cardinalboats.alias

import net.cardinalboats.generated.ModInfo
import net.minecraft.client.KeyMapping
import net.minecraft.resources.Identifier


val RADIANS_PER_DEGREE = (Math.PI.toFloat() / 180f);

val KEY_BINDING_CATEGORY = KeyMapping.Category.register(Identifier.tryBuild(ModInfo.MOD_ID, "binding_category")!!)

