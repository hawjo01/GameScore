plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.dagger.hilt) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
}

configurations.all {
    resolutionStrategy {
        // Because CVE-2024-7254
        force("com.google.protobuf:protobuf-kotlin:3.25.5")
        // Because CVE-2024-7254
        force("com.google.protobug:protobuf-java:3.25.5")
    }
}