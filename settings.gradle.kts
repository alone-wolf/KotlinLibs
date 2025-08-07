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
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

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
            from("io.ktor:ktor-version-catalog:3.2.2")
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
include(":JiebaJvm")
include(":AndLibSample")
include(":KtorServerJvm")

//includeBuild("../KotlinLibs")
//implementation("top.writerpass.libs:KMPLibrary:1.0.0")
//implementation("top.writerpass.libs:CMPLibrary:1.0.0")
//implementation("top.writerpass.libs:AndLib:1.0.0")
//implementation("top.writerpass.libs:JiebaJvm:1.0.0")
include(":OSPaths")
include(":FullKMP")
include(":OSDetect")
include(":Excel")
include(":KLogger")
