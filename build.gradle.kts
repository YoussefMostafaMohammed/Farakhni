plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.0" apply false  // Add this
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")  // Correct version
    }
}