plugins {
    alias(libs.plugins.android.application)
    kotlin("plugin.serialization") version "2.1.0"
}

android {
    namespace = "dev.faridguliyev.sample_app"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "dev.faridguliyev.sample_app"
        minSdk = 24
        targetSdk = 36
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":replaydroid"))
    implementation("com.github.faridGuliyew:replaydroid:100bfde73b")
}

val ktor_version = "3.4.1"

dependencies {
    // 1. Core Ktor Client
    implementation("io.ktor:ktor-client-core:$ktor_version")

    // 2. The Engine (CIO is the standard coroutine-based engine)
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")

    // 3. Content Negotiation (The plugin that handles the "handshake")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    // 4. JSON Serializer for Kotlinx Serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // 5. Logging (Optional, but highly recommended for debugging)
    implementation("io.ktor:ktor-client-logging:$ktor_version")
}