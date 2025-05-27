plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lostandfound"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lostandfound"
        minSdk = 30
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Core dependencies
    implementation(libs.appcompat) // AppCompat library
    implementation(libs.material)  // Material components library
    implementation(libs.activity)  // Activity library
    implementation(libs.constraintlayout) // ConstraintLayout library

    // Testing libraries
    testImplementation(libs.junit)         // Unit testing library
    androidTestImplementation(libs.ext.junit) // JUnit extension for Android tests
    androidTestImplementation(libs.espresso.core) // Espresso for UI tests

    // CardView library
    implementation("androidx.cardview:cardview:1.0.0") // CardView for your item details
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
}
