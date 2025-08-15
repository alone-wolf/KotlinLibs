@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.multiplatform.android.library)
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
        namespace = "top.writerpass.kotlinlibs.kmplibrary"
        compileSdk = 33
        minSdk = 24

        withJava() // enable java compilation support
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }


    sourceSets {
        jvmMain.dependencies {}
        val commonMain by getting {
            dependencies {
//                implementation(kotlin("stdlib-jdk8"))

                //put your multiplatform dependencies here
//                implementation(compose.runtime)
//                implementation(compose.foundation)
//                implementation(compose.material)
//                implementation(compose.material3)
//                implementation(compose.ui)
//                implementation(compose.components.resources)
//                implementation(compose.components.uiToolingPreview)
                implementation(libs.kotlinx.coroutines.core)

//                implementation("co.touchlab:kermit:2.0.4")


                implementation(kotlincrypto.bitops.bits)
                implementation(kotlincrypto.bitops.endian)

//                implementation(kotlincrypto.hash.blake2)
//                implementation(kotlincrypto.hash.md)
//                implementation(kotlincrypto.hash.sha1)
                implementation(kotlincrypto.hash.sha2)
//                implementation(kotlincrypto.hash.sha3)

                implementation(libs.kotlinx.datetime)
            }
        }

        wasmJsMain.dependencies {
//            implementation("org.jetbrains.kotlinx:kotlinx-browser:0.9.0")
        }

//        val commonTest by getting {
//            dependencies {
//                implementation(libs.kotlin.test)
//            }
//        }
    }
}
