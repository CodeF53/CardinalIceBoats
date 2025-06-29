import org.anti_ad.gradle.plugins.libipn.base.modId

val cloth_config_version: String by project
logger.lifecycle("""
    mod-id: $modId
""".trimIndent())
repositories {
    maven ("https://maven.shedaniel.me/")
}


dependencies {
    modImplementation ("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

plugins {
    id("libipn-gradle")
}

libIPN {
    jarPostProcessConfig = {
        this.advzipArguments = mutableListOf("-4", "-z", "-i", "100")
    }
}
