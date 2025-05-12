plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")  // Apply plugin here
}

android {
    namespace = "com.example.farakhni"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.farakhni"  // Must match package name in google-services.json
        minSdk = 24
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity)
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))  // BoM manages versions
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")  // Use KTX version
    implementation("com.google.android.gms:play-services-auth:21.1.0")  // Updated version

    implementation("com.google.firebase:firebase-analytics:22.4.0")
    // Room
    implementation("androidx.room:room-runtime:2.7.1")
    annotationProcessor("androidx.room:room-compiler:2.7.1")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.22")

    // Credentials
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Json
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.firebase:firebase-firestore:25.1.4")
    implementation("com.airbnb.android:lottie:6.3.0")

}