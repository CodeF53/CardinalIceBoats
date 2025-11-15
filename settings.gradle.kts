
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral().content {
            excludeGroup("net.jodah")
        }
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        maven { url = uri("https://maven.minecraftforge.net") }
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://maven.neoforged.net/releases/") }
        //maven { url = uri("https://plugins.gradle.org/m2/") }

        maven {
            name = "libIPN-Snapshots"
            mavenContent {
                snapshotsOnly()
            }
            content {
                includeGroup ("libipn-gradle")
                includeGroup ("org.anti_ad.mc")
                includeGroup ("org.anti_ad.mc.plugins")
                includeGroup ("ca.solo-studios")
                includeGroup ("org.ipnmod.easymod")
            }

            url = uri("https://maven.ipn-mod.org/snapshots")
        }
        maven {
            name = "libIPN-Releases"
            mavenContent {
                releasesOnly()
            }
            content {
                includeGroup ("libipn-gradle")
                includeGroup ("org.anti_ad.mc")
                includeGroup ("org.anti_ad.mc.plugins")
                includeGroup ("ca.solo-studios")
                includeGroup ("org.ipnmod.easymod")
            }
            url = uri("https://maven.ipn-mod.org/releases")
        }

    }
}

dependencyResolutionManagement {
    repositories {

        gradlePluginPortal()
        google()
        maven { url = uri("https://maven.minecraftforge.net") }
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://maven.neoforged.net/releases/") }
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }

        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net")
        }

        maven {
            name = "libIPN-Snapshots"
            mavenContent {
                snapshotsOnly()
            }
            content {
                includeGroup ("libipn-gradle")
                includeGroup ("org.anti_ad.mc")
                includeGroup ("org.anti_ad.mc.plugins")
                includeGroup ("ca.solo-studios")
                includeGroup ("org.ipnmod.easymod")
            }
            url = uri("https://maven.ipn-mod.org/snapshots")
        }
        maven {
            name = "libIPN-Releases"
            mavenContent {
                releasesOnly()
            }
            content {
                includeGroup ("libipn-gradle")
                includeGroup ("org.anti_ad.mc")
                includeGroup ("org.anti_ad.mc.plugins")
                includeGroup ("ca.solo-studios")
                includeGroup ("org.ipnmod.easymod")
            }
            url = uri("https://maven.ipn-mod.org/releases")
        }
        exclusiveContent {
            forRepository {
                maven { url = mavenCentral().url }
            }
            filter {
                includeVersion("org.lwjgl", "lwjgl-freetype", "3.3.3")
            }
        }

    }
}

rootProject.name = "CardinalIceBoats"

include(":platforms:fabric-1.18.2")
include(":platforms:fabric-1.19.2")
include(":platforms:fabric-1.20.1")
include(":platforms:fabric-1.21.1")
include(":platforms:fabric-1.21.10")
include(":platforms:neoforge-1.21.10")
include(":platforms:neoforge-1.21.1")
include(":platforms:forge-1.18.2")
include(":platforms:forge-1.19.2")
include(":platforms:forge-1.20.1")
include(":platforms:forge-1.21.1")


plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0+"
}

