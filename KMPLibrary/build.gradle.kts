@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
//    alias(libs.plugins.compose)
//    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.android.kotlin.multiplatform.library)
//    alias(libs.plugins.android.library)
//    alias(libs.plugins.vanniktech.mavenPublish)
//    `maven-publish`
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
        //withHostTestBuilder {}.configure {}
        //withDeviceTestBuilder {
        //    sourceSetTreeName = "test"
        //}

//        compilations.configureEach {
//            compilerOptions.configure {
//                jvmTarget.set(
//                    org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8
//                )
//            }
//        }
    }
//    androidTarget {
//        publishLibraryVariants("release")
//        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }
//    androidTarget {
//        publishLibraryVariants("release")
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }

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

//                implementation(kotlincrypto.macs.blake2)
//                implementation(kotlincrypto.macs.hmac.md)
//                implementation(kotlincrypto.macs.hmac.sha1)
//                implementation(kotlincrypto.macs.hmac.sha2)
//                implementation(kotlincrypto.macs.hmac.sha3)
//                implementation(kotlincrypto.macs.kmac)
//
//                implementation(kotlincrypto.random.crypto.rand)
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

//android {
//    namespace = "top.writerpass.kotlinlibs.kmplibrary"
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//    defaultConfig {
//        minSdk = libs.versions.android.minSdk.get().toInt()
////        compileSdk = libs.versions.android.compileSdk.get().toInt()
//        //noinspection OldTargetApi
//        // targetSdk = libs.versions.android.targetSdk.get().toInt()
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}

//publishing {
//    publications {
//        create<MavenPublication>("default") {
//            from(components["kotlin"])
//        }
//    }
//}

//mavenPublishing {
//    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
//
//    signAllPublications()
//
//    coordinates(group.toString(), "library", version.toString())
//
//    pom {
//        name = "My library"
//        description = "A library."
//        inceptionYear = "2024"
//        url = "https://github.com/kotlin/multiplatform-library-template/"
//        licenses {
//            license {
//                name = "XXX"
//                url = "YYY"
//                distribution = "ZZZ"
//            }
//        }
//        developers {
//            developer {
//                id = "XXX"
//                name = "YYY"
//                url = "ZZZ"
//            }
//        }
//        scm {
//            url = "XXX"
//            connection = "YYY"
//            developerConnection = "ZZZ"
//        }
//    }
//}

//dependencyResolutionManagement {
//
//}