plugins {
    // Apply the Android application plugin
    alias(libs.plugins.android.application)
    // Apply the Kotlin Android plugin
    alias(libs.plugins.kotlin.android)
}

android {
    // Define the namespace for the app
    namespace = "com.example.tictactoe"
    // Set the SDK version to compile against
    compileSdk = 35

    buildFeatures {
        // Enable view binding
        viewBinding = true
    }

    defaultConfig {
        // Unique application ID
        applicationId = "com.example.tictactoe"
        // Minimum SDK version the app supports
        minSdk = 24
        // Target SDK version the app is tested against
        targetSdk = 35
        // Version code for the app
        versionCode = 1
        // Version name for the app
        versionName = "1.0"

        // Specify the test instrumentation runner
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Disable code shrinking for the release build
            isMinifyEnabled = false
            // Proguard configuration files
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Set the Java source compatibility
        sourceCompatibility = JavaVersion.VERSION_11
        // Set the Java target compatibility
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        // Set the Kotlin JVM target version
        jvmTarget = "11"
    }
}

dependencies {
    // Core Android KTX library
    implementation(libs.androidx.core.ktx)
    // Android app compatibility library
    implementation(libs.androidx.appcompat)
    // Material design library
    implementation(libs.material)
    // Android activity library
    implementation(libs.androidx.activity)
    // Constraint layout library
    implementation(libs.androidx.constraintlayout)
    // Glide image loading library
    implementation (libs.glide)
    // Annotation processor for Glide
    annotationProcessor (libs.compiler)
    // JUnit testing library
    testImplementation(libs.junit)
    // AndroidX JUnit testing library
    androidTestImplementation(libs.androidx.junit)
    // AndroidX Espresso testing library
    androidTestImplementation(libs.androidx.espresso.core)
}
