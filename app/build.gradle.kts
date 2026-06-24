plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.vinted.demovinted"

    defaultConfig {
        applicationId = "com.vinted.demovinted"
        minSdk = libs.versions.minSdk.get().toInt()
        compileSdk = libs.versions.compileSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompact)
    implementation(libs.androidx.constaintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.recycleview)
    implementation(libs.material)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi.convertor)
    implementation(libs.retrofit.rxadapter)

    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    implementation(libs.hilt)
    ksp(libs.hilt.android.compiler)

    implementation(libs.glide)
    ksp(libs.glide.compiler)

    implementation(libs.kirich1409.viewbindingpropertydelegate)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.turbin)
    androidTestImplementation(libs.androidx.espresso)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.junit.ext)
}