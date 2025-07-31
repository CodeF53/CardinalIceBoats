package net.cardinalboats.mixinplugins

import com.llamalad7.mixinextras.MixinExtrasBootstrap
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

@Suppress("EmptyFunctionBlock")
class CIBMixinPlugin: IMixinConfigPlugin {

    private val logger: Logger = LogManager.getLogger("CIBMixinConfigPlugin")

    override fun onLoad(mixinPackage: String?) {
        logger.warn("CIBMixinPlugin: Loading CIB MixinPlugin")
        MixinExtrasBootstrap.init()
    }

    override fun getRefMapperConfig(): String? {
        return null;
    }

    override fun shouldApplyMixin(targetClassName: String?,
                                  mixinClassName: String?): Boolean {
        return true;
    }

    override fun acceptTargets(myTargets: Set<String?>?,
                               otherTargets: Set<String?>?) {

    }

    override fun getMixins(): List<String?>? {
        return null;
    }

    override fun preApply(targetClassName: String?,
                          targetClass: ClassNode?,
                          mixinClassName: String?,
                          mixinInfo: IMixinInfo?) {

    }

    override fun postApply(targetClassName: String?,
                           targetClass: ClassNode?,
                           mixinClassName: String?,
                           mixinInfo: IMixinInfo?) {

    }
}
