import com.android.build.api.dsl.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.multiplatform.android.library)
}

group = "top.writerpass.libs"
version = "1.0.0"

kotlin {
    jvm()
    androidLibrary {
        namespace = "top.writerpass.ktorserverjvm"
        compileSdk = 36
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(ktorLibs.server.core)
            api(ktorLibs.server.cio)
            api(ktorLibs.server.cors)
            api(ktorLibs.server.sse)
            api(ktorLibs.server.contentNegotiation)
            api(ktorLibs.server.websockets)
            api(ktorLibs.server.callLogging)
            api(ktorLibs.server.auth)
            api(ktorLibs.server.defaultHeaders)
            api(ktorLibs.server.resources)
            api(ktorLibs.server.statusPages)
            api(ktorLibs.server.compression)
            implementation(libs.kotlinx.datetime)
            implementation(ktorLibs.serialization.kotlinx.json)
        }
        jvmMain.dependencies {


        }
        androidMain.dependencies {
        }
    }
}
