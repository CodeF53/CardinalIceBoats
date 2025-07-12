import com.modrinth.minotaur.dependencies.ModDependency
import masecla.modrinth4j.model.version.ProjectVersion.*
import org.anti_ad.gradle.plugins.libipn.base.DeploySites
import org.anti_ad.gradle.plugins.libipn.base.JarPostProcess

val cloth_config_version: String by project

repositories {
    maven ("https://maven.shedaniel.me/")
}


dependencies {
    implementation ("me.shedaniel.cloth:cloth-config-neoforge:$cloth_config_version")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

plugins {
    alias(libs.plugins.libipnGradle)
    alias(libs.plugins.modrinth)
}

libIPN {
    this.enableShadow = false
    this.enableProGuard = false
    jarPostProcessConfig = {
        this.advzipArguments = listOf("-4", "-z", "-i", "100")
    }
    val deployVersionsMap = mutableMapOf(DeploySites.MODRINTH to listOf("1.21.6", "1.21.7"),
                                         DeploySites.CURSEFORGE to listOf("1.21.6", "1.21.7"))

    supportedMCVersions.set(deployVersionsMap)

}

afterEvaluate {


    modrinth {
        val mod_loader = libIPN.modLoader.get().removeSurrounding("\"")
        val mod_version = libIPN.modVersion.get().removeSurrounding("\"")
        val minecraft_version = libIPN.supportedMinecraftVersionMin.get().removeSurrounding("\"")

        this.failSilently.set(true)

        if (System.getenv("IPNEXT_RELEASE") != null) {
            token.set(System.getenv("MODRINTH_TOKEN"))
        }

        projectId.set("1m9s2ZhL")
        versionNumber.set("$mod_loader-$minecraft_version-$mod_version") // Will fail if Modrinth has this version already
        val postprocessedremappedJarFile = tasks.named<JarPostProcess>("libIPN-JarPostProcess").get().outputs.files.first()
        uploadFile.set(postprocessedremappedJarFile as Any) // This is the java jar task. If it can't find the jar, try 'jar.outputs.getFiles().asPath' in place of 'jar'
        logger.lifecycle("Modrinth upload file: ${postprocessedremappedJarFile.path}")
        gameVersions.addAll(libIPN.supportedMCVersions.get()[DeploySites.MODRINTH] as List<String>)
        logger.lifecycle("""
        +*************************************************+
        Will release ${postprocessedremappedJarFile.path}
        For $mod_loader $minecraft_version
        +*************************************************+
        """.trimIndent())
        versionName.set("CardinalIceBoats $mod_version for $mod_loader $minecraft_version")
        project.rootDir.resolve("description/out/pandoc-release_notes.md").takeIf { it.exists() }?.let {
            this.changelog.set(it.readText())
        }

        loaders.add(mod_loader)
        //ordsPcFz   -> kotlin for forge
        //9s6osm5g   -> cloth config api
        dependencies.set(mutableListOf(ModDependency("ordsPcFz", "required"),
                                       ModDependency("9s6osm5g", "required")))

        this.versionType.set(VersionType.RELEASE.name)
    }
}
