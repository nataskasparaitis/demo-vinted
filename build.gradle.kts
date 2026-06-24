plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
}

tasks.register("clean") {
    delete(rootProject.buildDir)
}

detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom("detekt/config.yml")
    basePath = projectDir.absolutePath
    autoCorrect = true
}

dependencies {
    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.rules)
}