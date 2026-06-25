plugins { // turn on the plugins this module needs
    alias(libs.plugins.android.application) // build an Android app
    alias(libs.plugins.kotlin.android) // compile Kotlin
    alias(libs.plugins.ksp) // run annotation code generators
    alias(libs.plugins.kotlin.parcelize) // generate Parcelable code
    alias(libs.plugins.dagger.hilt.android) // enable Hilt DI
}

android { // Android-specific build settings
    namespace = "com.vinted.demovinted" // package used for the generated R class

    defaultConfig { // core app identity and build values
        applicationId = "com.vinted.demovinted" // unique app id on the device
        minSdk = libs.versions.minSdk.get().toInt() // lowest supported Android (26)
        compileSdk = libs.versions.compileSdk.get().toInt() // API level compiled against (35)
        targetSdk = libs.versions.targetSdk.get().toInt() // API level targeted (35)
        versionCode = 1 // internal build number
        versionName = "1.0" // human-readable version name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // runner for on-device tests
    }

    buildTypes { // settings per build variant
        release { // the production build
            isMinifyEnabled = false // do not shrink/obfuscate the code
            proguardFiles( // shrink-rule files (only used if minify is on)
                getDefaultProguardFile("proguard-android-optimize.txt"), // Android's default rules
                "proguard-rules.pro" // this module's custom rules
            )
        }
    }

    compileOptions { // Java bytecode settings
        targetCompatibility = JavaVersion.VERSION_17 // produce Java 17 bytecode
    }

    kotlinOptions { // Kotlin compiler settings
        jvmTarget = JavaVersion.VERSION_17.toString() // target the Java 17 VM
    }

    buildFeatures { // optional build features
        viewBinding = true // generate a typed binding class for each layout
    }
}

dependencies { // libraries this app uses (by catalog nickname)
    implementation(libs.androidx.appcompact) // compatible UI base classes
    implementation(libs.androidx.constaintlayout) // constraint-based layouts
    implementation(libs.androidx.core.ktx) // Kotlin Android extensions
    implementation(libs.androidx.fragment) // Fragment support
    implementation(libs.androidx.lifecycle.livedata) // LiveData
    implementation(libs.androidx.lifecycle.viewmodel) // ViewModel
    implementation(libs.androidx.recycleview) // list/grid widget
    implementation(libs.material) // Material design components

    implementation(libs.retrofit) // HTTP client
    implementation(libs.retrofit.moshi.convertor) // JSON <-> object converter for Retrofit
    implementation(libs.retrofit.rxadapter) // RxJava adapter (declared, unused)

    implementation(libs.moshi) // JSON parser
    implementation(libs.moshi.kotlin) // Moshi support for Kotlin classes

    implementation(libs.hilt) // dependency-injection runtime
    ksp(libs.hilt.android.compiler) // Hilt code generator (build-time only)

    implementation(libs.glide) // image loading
    ksp(libs.glide.compiler) // Glide code generator (build-time only)

    implementation(libs.kirich1409.viewbindingpropertydelegate) // ViewBinding one-liner helper

    testImplementation(libs.coroutines.test) // coroutine test tools (JVM tests)
    testImplementation(libs.junit) // unit-test framework (JVM tests)
    testImplementation(libs.turbin) // Flow testing helper (JVM tests)
    androidTestImplementation(libs.androidx.espresso) // UI testing (on-device)
    androidTestImplementation(libs.androidx.espresso.contrib) // extra Espresso actions (on-device)
    androidTestImplementation(libs.androidx.junit.ext) // JUnit runner (on-device)
}