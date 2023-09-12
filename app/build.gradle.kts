import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "custom.push.notifications"
    compileSdk = 33
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "custom.push.notifications"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            val localProperties = Properties()
            localProperties.load(FileInputStream(rootProject.file("local.properties")))
            buildConfigField("String", "API_KEY", "${localProperties["API_KEY"]}")
        }
        release {
            val localProperties = Properties()
            localProperties.load(FileInputStream(rootProject.file("local.properties")))
            buildConfigField("String", "API_KEY", "${localProperties["API_KEY"]}")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {

    }
}

dependencies {

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //FCM Service
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.2")

    // JetFireStore
    implementation("com.github.raipankaj:JetFirestore:1.0.2")

    //Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.0-alpha01")

    //CoilCompose
    implementation("io.coil-kt:coil-compose:2.2.2")


    //Dagger Hilt
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.kapt)
    implementation(libs.dagger.hilt.navigation)

    //Ktor Client
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.gson)
    implementation(libs.ktor.client.cio)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.1")
    implementation(libs.play.services.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}