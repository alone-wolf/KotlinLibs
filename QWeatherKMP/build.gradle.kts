plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.multiplatform.android.library)
    alias(libs.plugins.kotlin.serialization)
}

group = "top.writerpass.libs"
version = "1.0.0"

kotlin {
    jvm()
//    wasmJs()
    androidLibrary {
        namespace = "top.writerpass.kotlinlibs.kmplibrary"
        compileSdk = 33
        minSdk = 24

        withJava()
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
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(ktorLibs.client.core)
                implementation(ktorLibs.client.cio)
                implementation(ktorLibs.serialization.kotlinx.json)
                implementation(ktorLibs.client.auth)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(ktorLibs.client.logging)
                implementation(ktorLibs.client.encoding)
                implementation("co.touchlab:kermit:2.0.8")


                implementation(project(":KMPLibrary"))
                implementation(project(":KMPResLoader"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(ktorLibs.client.java)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(ktorLibs.client.android)
            }
        }
    }
}
