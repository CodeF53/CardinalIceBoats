import masecla.modrinth4j.model.version.ProjectVersion.*
import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val cloth_config_version: String by project

repositories {
    maven ("https://maven.shedaniel.me/")
}

val fg: DependencyManagementExtension = project.extensions["fg"] as DependencyManagementExtension

dependencies {
    implementation (fg.deobf("me.shedaniel.cloth:cloth-config-forge:$cloth_config_version"))
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0")?.let { compileOnly(it) }
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.5.0")) {
        jarJar.ranged(this, "[0.5.0,)")
    }
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
        this.failSilently.set(true)

        if (System.getenv("IPNEXT_RELEASE") != null) {
            token.set(System.getenv("MODRINTH_TOKEN"))
        }
        this.versionType.set(VersionType.RELEASE.name)
        //1.20.1 neoforge is the "same" as forge
        this.loaders.add("neoforge")
    }
}
