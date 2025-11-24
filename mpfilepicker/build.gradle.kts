@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.multiplatform.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
}

val readableName = "Multiplatform File Picker"
group = "top.writerpass.libs"
version = "1.0.0"

extra.apply {
    set("isReleaseVersion", !(version as String).endsWith("SNAPSHOT"))
}

kotlin {
    androidLibrary {
        compileSdk = 36
        namespace = "top.writerpass.libs.mpfilepicker"
    }
//    androidLibrary {
////        publishLibraryVariants("release")
//        namespace = "com.darkrockstudios.libraries.mpfilepicker"
////        compileSdk = libs.versions.android.compile.sdk.get().toInt()
////        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
////        defaultConfig {
////            minSdk = libs.versions.android.min.sdk.get().toInt()
////        }
////        compileOptions {
////            sourceCompatibility = JavaVersion.VERSION_17
////            targetCompatibility = JavaVersion.VERSION_17
////        }
//    }

    jvm()

    js(IR) {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    macosX64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "MPFilePicker"
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            api(compose.uiTooling)
            api(compose.preview)
            api(compose.material)
//            api(libs.androidx.appcompat)
//            api(libs.androidx.core.ktx)
//            api(libs.compose.activity)
            api(libs.kotlinx.coroutines.android)
            implementation(libs.androidx.activity.ktx)
        }

        jvmMain.dependencies {
            api(compose.uiTooling)
            api(compose.preview)
            api(compose.material)

            val lwjglVersion = "3.3.1"
            listOf("lwjgl", "lwjgl-tinyfd").forEach { lwjglDep ->
                implementation("org.lwjgl:${lwjglDep}:${lwjglVersion}")
                listOf(
                    "natives-windows",
                    "natives-windows-x86",
                    "natives-windows-arm64",
                    "natives-macos",
                    "natives-macos-arm64",
                    "natives-linux",
                    "natives-linux-arm64",
                    "natives-linux-arm32"
                ).forEach { native ->
                    runtimeOnly("org.lwjgl:${lwjglDep}:${lwjglVersion}:${native}")
                }
            }
        }
//        val jvmTest by getting
        val jsMain by getting
        val wasmJsMain by getting
    }

    @Suppress("OPT_IN_USAGE")
    compilerOptions {
        freeCompilerArgs = listOf("-Xexpect-actual-classes")
    }
}

//android {
//    namespace = "com.darkrockstudios.libraries.mpfilepicker"
//    compileSdk = libs.versions.android.compile.sdk.get().toInt()
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    defaultConfig {
//        minSdk = libs.versions.android.min.sdk.get().toInt()
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//}

//compose.experimental {
//    web.application {}
//}
