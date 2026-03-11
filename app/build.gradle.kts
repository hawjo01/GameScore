plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.google.devtools.ksp)
}

val signingKeystore = file("signing_keystore.jks")

val nettyVersion = "4.1.129.Final"
val joseVersion = "0.9.6"
val commonsLang3Version = "3.18.0"
val httpClientVersion = "4.5.13"

configurations.all {
    resolutionStrategy.force(
        // Because CVE-2024-7254
        "com.google.protobuf:protobuf-kotlin:3.25.5",
        // Because CVE-2024-7254
        "com.google.protobug:protobuf-java:3.25.5",
        // Because Netty has multiple CVE's
        "io.netty:netty-buffer:$nettyVersion",
        "io.netty:netty-codec-http2:$nettyVersion",
        "io.netty:netty-code-http:$nettyVersion",
        "io.netty:netty-code-socks:$nettyVersion",
        "io.netty:netty-code:$nettyVersion",
        "io.netty:netty-common:$nettyVersion",
        "io.netty:netty-handler-proxy:$nettyVersion",
        "io.netty:netty-handler:$nettyVersion",
        "io.netty:netty-resolver:$nettyVersion",
        "io.netty:netty-transport-native-unix-common:$nettyVersion",
        "io.netty:netty-transport:$nettyVersion",
        "org.bitbucket.b_c:jose:$joseVersion",
        "org.apache.commons:commons-lang3:$commonsLang3Version",
        "org.apache.httpcomponents:httpclient:$httpClientVersion"
    )
}

android {
    namespace = "net.hawkins.gamescore"
    compileSdk = 36

    defaultConfig {
        applicationId = "net.hawkins.gamescore"
        minSdk = 34
        targetSdk = 36
        val versionCodeFromCI = System.getenv ("VERSION_CODE") ?: "4"
        versionCode = versionCodeFromCI.toInt()
        versionName = "2.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = signingKeystore
            storePassword = System.getenv ("KEY_STORE_PASSWORD" )
            keyAlias = System.getenv ( "KEY_ALIAS" )
            keyPassword = System.getenv ("KEY_PASSWORD" )
        }
    }
    buildTypes {
        release {
            ndk.debugSymbolLevel = "FULL"
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (signingKeystore.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }

    lint {
        sarifReport = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.view.model.ktx)
    implementation(libs.androidx.lifecycle.view.model.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.google.gson)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.google.dagger.hilt)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.lottie.compose)
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    ksp(libs.google.dagger.hilt.compiler)
    ksp(libs.kotlin.metadata.jvm)
}