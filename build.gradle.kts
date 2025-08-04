import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.kotlin.dsl.support.serviceOf
import java.io.ByteArrayOutputStream


val versionObj = Version("2", "0", "2", System.getenv("IPNEXT_RELEASE") == null)

plugins {
    idea
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlinSer) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.libipnGradle)
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}



subprojects {
    group = "net.cardinalboats"
    apply {
        plugin(rootProject.libs.plugins.detekt.get().pluginId)
    }

    detekt {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    }
    version = versionObj.toCleanString()

}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}


/**
 * Version class that does version stuff.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Version(val major: String,
              val minor: String,
              val revision: String,
              val preRelease: Boolean = false) {

    val execOperations: ExecOperations = project.serviceOf()

    private var gitVersionString: String = ""

    fun Project.getGitHash(): String {
        if (gitVersionString.isNotEmpty()) {
            return gitVersionString
        }
        val stdout = ByteArrayOutputStream()
        val exitCode = execOperations.exec {
            commandLine = mutableListOf("git", "rev-parse", "--short", "HEAD")
            standardOutput = stdout
            this.isIgnoreExitValue = true
        }.exitValue
        return if (exitCode == 0) {
            gitVersionString = stdout.toString().trim()
            gitVersionString

        } else {
            gitVersionString = "not-a-git-repo"
            gitVersionString
        }

    }

    val gitHash
        get() = getGitHash()

    override fun toString(): String {
        return if (!preRelease) "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision-BETA+C$gitHash-SNAPSHOT"
    }

    fun toCleanString(): String {
        return if (!preRelease) "$major.$minor.$revision"
        else //Only use git hash if it's a prerelease.
            "$major.$minor.$revision-SNAPSHOT"
    }

    fun isRelease() = !preRelease
}
