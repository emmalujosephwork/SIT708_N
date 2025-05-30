plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lafmap"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lafmap"
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation("com.google.maps.android:android-maps-utils:3.10.0")
    // Google Maps SDK
    implementation ("com.google.android.gms:play-services-maps:18.1.0")

    // Google Places SDK for Autocomplete
    implementation ("com.google.android.libraries.places:places:2.7.0")

    implementation ("com.google.android.gms:play-services-location:21.0.1")


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

