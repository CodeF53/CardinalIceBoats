import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlinSer) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.loom) apply false
    id("libipn-gradle") version "1.0.0-SNAPSHOT" apply false
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}



subprojects {
    group = "org.anti_ad.mc"
    apply {
        plugin(rootProject.libs.plugins.detekt.get().pluginId)
    }

    detekt {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    }
    version = "1.0.0"

}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}
