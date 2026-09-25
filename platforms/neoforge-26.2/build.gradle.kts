import com.modrinth.minotaur.dependencies.ModDependency
import masecla.modrinth4j.model.version.ProjectVersion.*

val cloth_config_version: String by project

repositories {
    maven ("https://maven.shedaniel.me/")
}


dependencies {
    implementation ("me.shedaniel.cloth:cloth-config-neoforge:$cloth_config_version")
}

plugins {
    alias(libs.plugins.easymod)
}

easyMod {
    enableShadow = false
    enableProGuard = false
    jarPostProcessConfig = {
        advzipArguments = listOf("-4", "-z", "-i", "100")
    }
}

afterEvaluate {
    modrinth {
        failSilently.set(true)
/*
        debugMode = true
        if (debugMode.get()) {
            token.set("INVALID")
        }
*/
        if (System.getenv("IPNEXT_RELEASE") != null) {
            token.set(System.getenv("MODRINTH_TOKEN"))
        }
        this.versionType.set(VersionType.RELEASE.name)
    }
}
