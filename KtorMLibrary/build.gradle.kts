@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(ktorLibs.plugins.ktor)
}


group = "top.writerpass.libs"
version = "1.0.0"

kotlin {
    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "top.writerpass.ktorm"
        compileSdk = 35
        minSdk = 24

//        withHostTestBuilder {
//        }

//        withDeviceTestBuilder {
//            sourceSetTreeName = "test"
//        }.configure {
//            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
//    val xcfName = "KtorMLibraryKit"
//    iosX64 {
//        binaries.framework {
//            baseName = xcfName
//        }
//    }
//
//    iosArm64 {
//        binaries.framework {
//            baseName = xcfName
//        }
//    }
//
//    iosSimulatorArm64 {
//        binaries.framework {
//            baseName = xcfName
//        }
//    }

    wasmJs()
    jvm()

//    js(IR) {
//        nodejs()
//    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
//                implementation(libs.kotlin.stdlib)
                // Add KMP dependencies here
//                implementation(libs.ktor.serialization.kotlinx.json)

//                implementation(libs.ktor.client.core)
//                implementation(libs.ktor.client.cio)
//                implementation(libs.ktor.client.content.negotiation)

                implementation(ktorLibs.serialization.kotlinx.json)

                implementation(ktorLibs.client.core)
                implementation(ktorLibs.client.cio)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(project(":KMPLibrary"))
            }
        }

//        commonTest {
//            dependencies {
//                implementation(libs.kotlin.test)
//            }
//        }

        jvmMain.dependencies {
            implementation(ktorLibs.server.core)
            implementation(ktorLibs.server.cio)
            implementation(ktorLibs.server.contentNegotiation)
            implementation(ktorLibs.server.cors)
            implementation(ktorLibs.server.compression)
            implementation(ktorLibs.server.websockets)
            implementation(ktorLibs.server.sse)
//            implementation(project(":KotlinLibrary"))
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                implementation(ktorLibs.server.core)
                implementation(ktorLibs.server.cio)
                implementation(ktorLibs.server.contentNegotiation)
                implementation(ktorLibs.server.cors)
                implementation(ktorLibs.server.compression)
                implementation(ktorLibs.server.websockets)
                implementation(ktorLibs.server.sse)
//                implementation(project(":KotlinLibrary"))
            }
        }

//        getByName("androidDeviceTest") {
//            dependencies {
//                implementation(libs.runner)
//                implementation(libs.core)
//                implementation(libs.junit)
//            }
//        }

//        iosMain {
//            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
//            }
//        }

        wasmJsMain {
            dependencies {

            }
        }
    }

}