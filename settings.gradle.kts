pluginManagement { // configure where Gradle gets its build plugins
    repositories { // sources to download plugins from
        google() // Google's repository (holds the Android Gradle plugin)
        mavenCentral() // the main public Java/Kotlin package repository
        gradlePluginPortal() // Gradle's own plugin repository
    }
}

dependencyResolutionManagement { // congifure where Gradle gets app libraries
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // only allow repos declared here (reproducible builds)
    repositories { // sources to download libraries from
        google() // Google's repository (AndroidX libraries)
        mavenCentral() // the main public library repository
    }
}


include(":app") // this build had one module, named "app"
rootProject.name = "Demo Vinted" // the overall project name
 