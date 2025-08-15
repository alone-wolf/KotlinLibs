plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()
    wasmJs()
    androidLibrary {
        namespace = "top.writerpass.kotlinlibs.kmplibrary"
        compileSdk = 33
        minSdk = 24

        withJava()
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
//                implementation("io.ktor:ktor-client-encoding:3.2.3")

                implementation(project(":KMPLibrary"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(ktorLibs.client.java)
            }
        }
        val androidMain by getting{
            dependencies{
                implementation(ktorLibs.client.android)
            }
        }
        val wasmJsMain by getting{
            dependencies{
                implementation(ktorLibs.client.js)
            }
        }
    }
}
