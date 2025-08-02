plugins {
    alias(libs.plugins.multiplatform)
}

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting {
            dependencies {
                api(project(":OSDetect"))
                implementation("net.java.dev.jna:jna:5.17.0") // 最新版本可查 Maven Central
                implementation("net.java.dev.jna:jna-platform:5.17.0") // 包含 Guid、ShlObj、Ole32 等

            }
        }
    }
}

group = "top.writerpass.libs"
version = "1.0.0"
