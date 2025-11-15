import masecla.modrinth4j.model.version.ProjectVersion.*

val cloth_config_version: String by project

repositories {
    maven ("https://maven.shedaniel.me/")
}

dependencies {
    modImplementation ("me.shedaniel.cloth:cloth-config-fabric:$cloth_config_version")
}

plugins {
    alias(libs.plugins.libipnGradle)
}

libIPN {
    enableShadow = false
    enableProGuard = false
    jarPostProcessConfig = {
        advzipArguments = listOf("-4", "-z", "-i", "100")
    }
}

afterEvaluate {
    modrinth {
        failSilently.set(true)
        if (System.getenv("IPNEXT_RELEASE") != null) {
            token.set(System.getenv("MODRINTH_TOKEN"))
        }
        versionType.set(VersionType.RELEASE.name)
    }
}
