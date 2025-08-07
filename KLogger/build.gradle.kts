plugins {
//    id("java-library")
//    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.multiplatform)
}
//java {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//}
kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
    }
}
