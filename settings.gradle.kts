
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        maven { url = uri("https://maven.minecraftforge.net") }
        maven {
            url = uri("https://maven.fabricmc.net/")
        }
        maven { url = uri("https://maven.neoforged.net/releases/") }
        maven { url = uri("https://plugins.gradle.org/m2/") }

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
            }
            url = uri("https://maven.ipn-mod.org/releases")
        }

    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        maven { url = uri("https://maven.minecraftforge.net") }
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://maven.neoforged.net/releases/") }
        maven { url = uri("https://plugins.gradle.org/m2/") }

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
            }
            url = uri("https://maven.ipn-mod.org/releases")
        }
    }


}

rootProject.name = "CardinalIceBoats"

include(":platforms:fabric-1.21.5")
//include(":platforms:forge-1.21.5")
//include(":platforms:neoforge-1.21.5")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.+"
}

