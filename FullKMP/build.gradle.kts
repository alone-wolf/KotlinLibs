@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)

}

kotlin {
    cocoapods {
        // Required properties
        // Specify the required Pod version here
        // Otherwise, the Gradle project version is used
        version = "1.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        name = "MyCocoaPod"

        framework {
            // Required properties
            // Framework name configuration. Use this property instead of deprecated 'frameworkName'
            baseName = "MyFramework"

            // Optional properties
            // Specify the framework linking type. It's dynamic by default.
            isStatic = false
            // Dependency export
            // Uncomment and specify another project module if you have one:
            // export(project(":<your other KMP module>"))
            transitiveExport = false // This is default.
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }
    // ----------- ✅ Common Targets -----------
    jvm()                // Java Virtual Machine
    androidTarget()           // Android
    js(IR) {
        browser()
        nodejs()
    }
    wasmJs()                  // WebAssembly（实验性）

    // ----------- ✅ Native Targets -----------

    androidNativeX64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()

    // --- Windows
    mingwX64("windowsX64")

    // --- Linux
    linuxX64("linuxX64")
    linuxArm64("linuxArm64")
    linuxArm32Hfp("linuxArm32Hfp")

    // --- macOS
    macosX64("macosX64")
    macosArm64("macosArm64")

    // --- iOS
    iosX64("iosX64")
    iosArm64("iosArm64")
    iosSimulatorArm64("iosSimArm64")

    // --- watchOS
    watchosX64("watchosX64")
    watchosArm64("watchosArm64")
    watchosSimulatorArm64("watchosSimArm64")

    // --- tvOS
    tvosX64("tvosX64")
    tvosArm64("tvosArm64")
    tvosSimulatorArm64("tvosSimArm64")

    // ----------- ✅ Source Sets -----------
    sourceSets {
        val commonMain by getting {
            dependencies {
                // 通用库可在此引入
                // implementation("...")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting
        val jvmTest by getting

        val androidMain by getting
//        val androidTest by getting

        val jsMain by getting
        val jsTest by getting

        val wasmJsMain by getting
        val wasmJsTest by getting

        val linuxX64Main by getting
        val linuxArm64Main by getting

        val macosX64Main by getting
        val macosArm64Main by getting

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimArm64Main by getting

        val windowsX64Main by getting
        val tvosX64Main by getting
        val watchosArm64Main by getting

        // 可按需求组织共享 sourceSets，如下所示：
        val nativeMain by creating {
            dependsOn(commonMain)
        }

        listOf(
            linuxX64Main,
            linuxArm64Main,
            macosX64Main,
            macosArm64Main,
            iosX64Main,
            iosArm64Main,
            iosSimArm64Main,
            windowsX64Main
        ).forEach {
            it.dependsOn(nativeMain)
        }
    }
}

// ----------- ✅ Android 配置（如启用 androidTarget）-----------
android {
    compileSdk = 34
    namespace = "your.name"

    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

// ----------- ✅ CocoaPods（如你要支持 iOS Framework）-----------
// apply(plugin = "org.jetbrains.kotlin.native.cocoapods")
// cocoapods {
//     summary = "Your KMP Library"
//     homepage = "https://your.domain"
//     ios.deploymentTarget = "14.1"
//     podfile = project.file("../iosApp/Podfile")
//     framework {
//         baseName = "SharedLib"
//     }
// }
