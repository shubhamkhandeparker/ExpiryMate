// app/build.gradle.kts (module-level)
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Compose compiler plugin for Kotlin 1.9.x
    id("kotlin-kapt")  // // 1) kapt plugin for annotation processors
    kotlin("plugin.compose") version "2.0.21"
}

android {
    namespace   = "com.example.tempexpirymate"
    compileSdk  = 35

    defaultConfig {
        applicationId        = "com.example.tempexpirymate"
        minSdk               = 24
        targetSdk            = 35
        versionCode          = 1
        versionName          = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        // Must match Compose UI 1.5.0 below
        kotlinCompilerExtensionVersion = "1.5.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    // AndroidX core + Activity-Compose
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Compose UI + Foundation + Tooling (all at 1.5.0)
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.foundation:foundation:1.5.0")
    implementation("androidx.compose.ui:ui-graphics:1.5.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")

    // Material 3 (includes SmallTopAppBar)
    implementation("androidx.compose.material3:material3:1.2.0")

    // Material Icons (Icons.Default.Search)
    implementation("androidx.compose.material:material-icons-extended:1.5.0")

    implementation("androidx.navigation:navigation-compose:2.6.0")


    // 2) Room runtime & KTX for coroutines
    implementation("androidx.room:room-runtime:2.7.1")
    implementation("androidx.room:room-ktx:2.7.1")

    // 3) Room compiler—must match your room-runtime version
    kapt("androidx.room:room-compiler:2.7.1")


}
