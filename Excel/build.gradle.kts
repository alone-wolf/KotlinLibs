plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    api("org.apache.poi:poi-ooxml:5.4.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.21")
}

group = "top.writerpass.libs"
version = "1.0.0"