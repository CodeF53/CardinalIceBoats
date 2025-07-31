import masecla.modrinth4j.model.version.ProjectVersion.*

val cloth_config_version: String by project

repositories {
    maven ("https://maven.shedaniel.me/")
}

dependencies {
    implementation ("me.shedaniel.cloth:cloth-config-neoforge:$cloth_config_version")
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

afterEvaluate {
    modrinth {
        this.failSilently.set(true)

        if (System.getenv("IPNEXT_RELEASE") != null) {
            token.set(System.getenv("MODRINTH_TOKEN"))
        }
        this.versionType.set(VersionType.RELEASE.name)
    }
}
