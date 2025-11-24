@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}
group = "top.writerpass.libs"
version = "1.0.0"
kotlin {

    jvm()
    wasmJs {
        browser()
        binaries.executable()
    }

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
                implementation(libs.kotlinx.datetime)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.androidx.navigation.compose)
//                api("io.github.dautovicharis:charts:2.0.0")
                implementation("com.composables:composetheme:1.1.0-alpha")


                implementation(project(":CMPLibrary"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api(compose.desktop.currentOs)
                api(libs.kotlinx.coroutines.swing)
                api(libs.kstore.file)
            }
        }
    }
}
 