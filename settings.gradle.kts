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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
        create("ktorLibs") {
            from("io.ktor:ktor-version-catalog:3.2.3")
        }
        create("kotlinLibs") {
            from(files("./gradle/KotlinLibs.versions.toml"))
        }
        create("databaseORM") {
            from(files("./gradle/DBORM.versions.toml"))
        }
        create("kotlincrypto") {
            // https://github.com/KotlinCrypto/version-catalog/blob/master/gradle/kotlincrypto.versions.toml
            from("org.kotlincrypto:version-catalog:0.8.0")
        }
    }
}

rootProject.name = "KotlinLibs"
include(":CMPLibrary")
include(":KMPLibrary")
include(":KotlinLibrary")
//include(":KtorLibrary")
//include(":KtorMLibrary")
include(":AndLib")
//include(":JiebaJvm")
//include(":AndLibSample")
//include(":KtorServerJvm")
//include(":OSPaths")
//include(":FullKMP")
//include(":OSDetect")
//include(":ExcelJvm")
//include(":KLogger")
//include(":CMPFramework")
//include(":CMPKLogger")
//include(":CMPDatePicker")
//include(":QWeatherKMP")
//include(":KtorClientKMP")
//includeBuild("../KotlinLibs")
//implementation("top.writerpass.libs:KMPLibrary:1.0.0")
//implementation("top.writerpass.libs:CMPLibrary:1.0.0")
//implementation("top.writerpass.libs:AndLib:1.0.0")
//implementation("top.writerpass.libs:JiebaJvm:1.0.0")

//include(":OSAccessKMP")
//include(":KtorUserCentre")
//include(":WebDavServer")
//include(":WebDavClient")
//include(":KMPResLoader")
//include(":CMPSample")
//include(":Rekuester")
//include(":RsyncKMP")
include(":mpfilepicker")
include(":FloatingWindowManager")
