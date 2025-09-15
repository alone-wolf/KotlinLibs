import com.android.build.api.dsl.androidLibrary

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.multiplatform.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
}

group = "top.writerpass.libs"
version = "1.0.0"

//android {
//    namespace = "top.writerpass.andlib"
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//    defaultConfig {
//        minSdk = libs.versions.android.minSdk.get().toInt()
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}

kotlin {
    androidLibrary {
        namespace = "top.writerpass.andlib"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
//        defaultConfig {
//            minSdk = libs.versions.android.minSdk.get().toInt()
//        }
//        compileOptions {
//            sourceCompatibility = JavaVersion.VERSION_11
//            targetCompatibility = JavaVersion.VERSION_11
//        }
    }
//    androidTarget()
    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.animation)
                implementation(libs.kotlinx.coroutines.core)
                implementation(project(":KMPLibrary"))
                implementation(project(":CMPLibrary"))
            }
        }
        androidMain {
            dependencies {
                implementation(libs.core.ktx)
                implementation(libs.appcompat)
//                implementation(libs.material)
                implementation(libs.lifecycle.runtime.ktx)
                implementation(libs.activity.compose)

                api(project.dependencies.platform("androidx.compose:compose-bom:2025.09.00"))
                api("androidx.compose.ui:ui")
                api("androidx.compose.ui:ui-graphics")
                api("androidx.compose.animation:animation")
                api("androidx.compose.ui:ui-tooling-preview")
                api("androidx.compose.material3:material3")
            }
        }
    }
}