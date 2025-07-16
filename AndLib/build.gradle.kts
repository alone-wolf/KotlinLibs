plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.android.library)
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    //alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

group = "top.writerpass.libs"
version = "1.0.0"

kotlin {
    androidLibrary {
        namespace = "top.writerpass.andlib"
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

    sourceSets {
        androidMain {
            dependencies {
                // Add Android-specific dependencies here
                implementation(libs.core.ktx)
                implementation(libs.appcompat)
                implementation(libs.material)
                implementation(libs.lifecycle.runtime.ktx)
                implementation(libs.activity.compose)
//                implementation(platform(libs.compose.bom))
                implementation(libs.ui)
                implementation(libs.ui.graphics)
//                implementation(libs.ui.tooling.preview)
//                implementation(libs.material3)
//                testImplementation(libs.junit.junit)
//                androidTestImplementation(libs.junit)
//                androidTestImplementation(libs.espresso.core)
//                androidTestImplementation(platform(libs.compose.bom))
//                androidTestImplementation(libs.ui.test.junit4)
//                debugImplementation(libs.ui.tooling)
//                debugImplementation(libs.ui.test.manifest)

                implementation(project(":KMPLibrary"))
                implementation(project(":CMPLibrary"))
            }
        }
    }
    // ... other targets (JVM, iOS, etc.) ...
}

//android {
//    namespace = "top.writerpass.andlib"
//    compileSdk = 35
//
//    defaultConfig {
////        applicationId = "top.writerpass.inputeventdispatcher"
//        minSdk = 24
////        targetSdk = 36
////        versionCode = 1
////        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
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
//    kotlinOptions {
//        jvmTarget = "11"
//    }
//    buildFeatures {
//        compose = true
//    }
//}

//dependencies {
//
//}