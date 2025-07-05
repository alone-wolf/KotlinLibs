enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
//plugins {
//    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
//}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    versionCatalogs {
        create("ktorLibs"){
            from("io.ktor:ktor-version-catalog:3.2.0")
        }
        create("kotlincrypto") {
            // https://github.com/KotlinCrypto/version-catalog/blob/master/gradle/kotlincrypto.versions.toml
            from("org.kotlincrypto:version-catalog:0.7.1")
        }
    }
}

rootProject.name = "KotlinLibs"
include(":CMPLibrary")
include(":KotlinLibrary")
include(":KtorLibrary")
include(":KMPLibrary")
include(":KtorMLibrary")
include(":AndLib")

////include(":KotlinLibrary")
////project(":KotlinLibrary").projectDir = File("../KotlinLibs/KotlinLibrary")
//
//include(":CMPLibrary")
//project(":CMPLibrary").projectDir = File("../KotlinLibs/CMPLibrary")
//
//include(":KMPLibrary")
//project(":KMPLibrary").projectDir = File("../KotlinLibs/KMPLibrary")
//
////include(":KtorLibrary")
////project(":KtorLibrary").projectDir = File("../KotlinLibs/KtorLibrary")
//
////include(":KtorMLibrary")
////project(":KtorMLibrary").projectDir = File("../KotlinLibs/KtorMLibrary")
//
////include(":AndroidLibrary")
////project(":AndroidLibrary").projectDir = File("../KotlinLibs/AndLib")