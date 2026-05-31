//import dev.detekt.gradle.Detekt
import org.gradle.kotlin.dsl.support.serviceOf
import java.io.ByteArrayOutputStream



plugins {
    idea
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kotlinSer) apply false
    //alias(libs.plugins.detekt)
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.easymod)
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}


modVersion {
    semanticVersion = "2.1.0"
    release = {
        System.getenv("IPNEXT_RELEASE") != null
    }
}



subprojects {
    group = "net.cardinalboats"
/*
    apply {
        plugin(rootProject.libs.plugins.detekt.get().pluginId)
    }

    detekt {
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    }
*/

}

/*
tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))
    }
}
*/
