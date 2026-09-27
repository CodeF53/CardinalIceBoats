@file:OptIn(ExperimentalAtomicApi::class)

package net.cardinalboats.alias

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@Suppress("MagicNumber")
const val RADIANS_PER_DEGREE = (Math.PI.toFloat() / 180f);

val KEY_BINDING_CATEGORY_REG: AtomicReference<KeyMapping.Category?> = AtomicReference(null)

val KEY_BINDING_CATEGORY: KeyMapping.Category by lazy {
    KEY_BINDING_CATEGORY_REG.load()!!
}
val KeyTypeKeyboard = InputConstants.Type.KEYBOARD
