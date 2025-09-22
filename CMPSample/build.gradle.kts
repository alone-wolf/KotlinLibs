import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.multiplatform.android.library)
//    alias(libs.plugins.kotlinJvm)
//    alias(libs.plugins.kotlin.serialization)
}

group = "top.writerpass.libs"
version = "1.0.0"

kotlin {
    jvm()
    wasmJs {
        browser()
        binaries.executable()
    }
    androidLibrary {
        namespace = "top.writerpass.cmplibrary"
        compileSdk = 36
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

//    androidLibrary {
//        compileSdk = 36
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }
//    androidTarget {
//        publishLibraryVariants("release")
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.navigation.compose)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.kstore.file)
            }
        }
//        val androidMain by getting {
//            dependsOn(jvmMain)
//            dependencies {
//
//            }
//        }
    }
}