import io.gitlab.arturbosch.detekt.Detekt

plugins {
    idea
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlinSer) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.shadow) apply false
    //alias(libs.plugins.loom) apply false
    alias(libs.plugins.libipnGradle)

    id("net.minecraftforge.gradle") version "(6.0.26,6.2]" apply false
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
    version = "2.0.1"

}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}
