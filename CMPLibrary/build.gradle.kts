
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

//import com.vanniktech.maven.publish.SonatypeHost
//import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
//import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.androidLibrary)
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
//    androidTarget()
//    androidTarget {
//        publishLibraryVariants("release")
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }
//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()
//    linuxX64()

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
                //put your multiplatform dependencies here
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
//                implementation("co.touchlab:kermit:2.0.4")

            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
//            implementation(libs.ktor.client.okhttp)
                implementation(libs.kstore.file)
            }
        }
//        desktopMain.dependencies {
//
//        }
//        val commonTest by getting {
//            dependencies {
//                implementation(libs.kotlin.test)
//            }
//        }
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("default") {
//            from(components["kotlin"])
//        }
//    }
//}

//android {
//    namespace = "org.jetbrains.kotlinx.multiplatform.library.template"
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//    defaultConfig {
//        minSdk = libs.versions.android.minSdk.get().toInt()
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
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
