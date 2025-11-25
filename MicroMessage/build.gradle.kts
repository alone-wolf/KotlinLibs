plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.multiplatform.android.library)
//    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "top.writerpass.libs"
version = "1.0.0"

kotlin {
    jvm()
//    wasmJs {
//        browser()
//        binaries.executable()
//    }
//    androidTarget()

//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64()
//    ).forEach {
//        it.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.logger.napier)

                implementation(project(":CMPLibrary"))
                implementation(project(":KMPLibrary"))

                // QRCode Generate
                // https://github.com/alexzhirkevich/qrose
                implementation("io.github.alexzhirkevich:qrose:1.0.1")

                // client
                implementation(ktorLibs.client.core)
                implementation(ktorLibs.client.cio)
                implementation(ktorLibs.client.serialization)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(ktorLibs.client.websockets)
                implementation(ktorLibs.client.auth)
                implementation(ktorLibs.client.logging)
                implementation(ktorLibs.client.encoding)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.kstore.file)

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")  // 或最新版本


                // server
                implementation("io.github.classgraph:classgraph:4.8.184")
                implementation(ktorLibs.server.core)
                implementation(ktorLibs.server.websockets)
                implementation(ktorLibs.server.csrf)
                implementation(ktorLibs.server.partialContent)
                implementation(ktorLibs.server.forwardedHeader)
                implementation(ktorLibs.server.defaultHeaders)
                implementation(ktorLibs.server.cors)
                implementation(ktorLibs.server.compression)
                implementation(ktorLibs.server.auth)
                implementation(ktorLibs.server.auth.jwt)
                implementation(ktorLibs.server.htmlBuilder)
                implementation(libs.kotlinx.html)
                implementation(ktorLibs.server.autoHeadResponse)
                implementation(ktorLibs.server.callLogging)
                implementation(ktorLibs.serialization.kotlinx.json)
                implementation(ktorLibs.server.contentNegotiation)
                implementation(libs.exposed.core)
                implementation(libs.exposed.dao)
                implementation(libs.exposed.jdbc)
                implementation(libs.h2)
                implementation(libs.koin.ktor)
                implementation(libs.koin.logger.slf4j)
                implementation(ktorLibs.server.rateLimit)
                implementation(ktorLibs.server.netty)
                implementation(ktorLibs.server.cio)
                implementation(libs.logback)
//                testImplementation(ktorLibs.server.testHost)
//                testImplementation(libs.kotlin.test.junit)
            }
        }
//        val androidMain by getting {
//            dependencies {}
//        }
    }
}

//android {
//    namespace = "top.writerpass.micromessage.client"
//    compileSdk = 36
//
//    defaultConfig {
//        applicationId = "top.writerpass.micromessage.client"
//        minSdk = 24
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}