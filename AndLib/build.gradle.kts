plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
}

group = "top.writerpass.libs"
version = "1.0.0"

android {
    namespace = "top.writerpass.andlib"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

kotlin {
    androidTarget()
    sourceSets {
        commonMain {
            dependencies {
                api(project(":KMPLibrary"))
                api(project(":CMPLibrary"))
            }
        }
        androidMain {
            dependencies {
                implementation(libs.core.ktx)
                implementation(libs.appcompat)
                implementation(libs.material)
                implementation(libs.lifecycle.runtime.ktx)
                implementation(libs.activity.compose)
            }
        }
    }
}