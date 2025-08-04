import com.android.kotlin.multiplatform.ide.models.serialization.androidTargetKey

plugins {
    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
}

kotlin {
    androidTarget()

    sourceSets{
        val androidMain by getting {
            dependencies {
                implementation(libs.core.ktx)
                implementation(libs.lifecycle.runtime.ktx)
                implementation(libs.activity.compose)
//                implementation(project(":AndLib"))
                implementation(project(":CMPLibrary"))
                implementation(project(":KMPLibrary"))
            }
        }
    }
}

android {
    namespace = "top.writerpass.sample"
    compileSdk = 36

    defaultConfig {
        applicationId = "top.writerpass.sample"
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
//    kotlinOptions {
//        jvmTarget = "11"
//    }
//    buildFeatures {
//        compose = true
//    }
}

//dependencies {
//    implementation(libs.core.ktx)
//    implementation(libs.lifecycle.runtime.ktx)
//    implementation(libs.activity.compose)
//    implementation(platform(libs.compose.bom))
//    implementation(libs.ui)
//    implementation(libs.ui.graphics)
//    implementation(libs.ui.tooling.preview)
//    implementation(libs.material3)

//    implementation(project(":AndLib"))
//}