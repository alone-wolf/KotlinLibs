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
    implementation(libs.kotlin.reflect)
    implementation(ktorLibs.client.core)
    implementation(ktorLibs.client.callId)
    implementation(ktorLibs.client.cio)
}

group = "top.writerpass.libs"
version = "1.0.0"