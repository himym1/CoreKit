plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    kotlin("kapt")
    id("maven-publish")
}

group = "com.github.himym1"
version = "1.0.5"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = group.toString()
                artifactId = "CoreKit"
                version = version
            }
        }
    }
}

android {
    namespace = "com.himym.corekit"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true
    }

}

dependencies {

    api(libs.bundles.appcompat)
    api(libs.bundles.lifecycle)
    api(libs.bundles.ui)
    api(libs.bundles.koinBundle)
    api(libs.bundles.networkAndStorage)
}