@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.multiplatform.android.library)
}

group = "top.writerpass.libs"
version = "1.0.0"

kotlin {
    jvm {
        compilations["main"].defaultSourceSet {
            resources.srcDir("src/jvmMain/resources")
        }
    }
    androidLibrary {
        namespace = "top.writerpass.kotlinlibs.kmplibrary"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(project(":KMPLibrary"))
        }

        jvmMain.dependencies {

        }

        androidMain.dependencies {

        }
    }
}

