val cloth_config_version: String by project

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
    alias(libs.plugins.libipnGradle)
}

libIPN {
    this.enableShadow = false
    this.enableProGuard = false
    jarPostProcessConfig = {
        this.advzipArguments = listOf("-4", "-z", "-i", "100")
    }
}
