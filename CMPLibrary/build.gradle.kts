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
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.ui)
                api(compose.components.resources)
                api(compose.components.uiToolingPreview)
                api(libs.kotlinx.coroutines.core)

                api(libs.androidx.navigation.compose)
//                api("io.github.dautovicharis:charts:2.0.0")
//                implementation("com.darkrockstudios:mpfilepicker:3.1.0")
                api(project(":mpfilepicker"))
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