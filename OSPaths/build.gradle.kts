plugins {
    alias(libs.plugins.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {}
        }
        val jvmMain by getting {
            dependencies {}
        }
    }
}

group = "top.writerpass.libs"
version = "1.0.0"
