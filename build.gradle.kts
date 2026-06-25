plugins { // turn build pligins on/off for the whole project
    alias(libs.plugins.android.application) apply false // make available to modules, do not apply here
    alias(libs.plugins.dagger.hilt.android) apply false // Hilt; appleid in the app module
    alias(libs.plugins.detekt) // the linter; applied at the root
    alias(libs.plugins.kotlin.android) apply false // Kotlin; applied in the app module
    alias(libs.plugins.kotlin.parcelize) apply false // Parcelable; applied in the app module
    alias(libs.plugins.ksp) apply false // codegen engine; applied in the app module
}

tasks.register("clean") { // define a task called "clean"
    delete(rootProject.buildDir) // that deletes the build output folder
}

detekt { // configure the linter
    toolVersion = libs.versions.detekt.get() // which detekt version to use
    config.setFrom("detekt/config.yml") // the rules file to read
    basePath = projectDir.absolutePath // base path used in report file names
    autoCorrect = true // auto-fix issues where possible
}

dependencies { // dependencies for the linter itself
    detektPlugins(libs.detekt.formatting) // add formating rules
    detektPlugins(libs.detekt.rules) // add extra library rules
}