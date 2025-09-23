import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
        jvmTarget = JvmTarget.JVM_11
    }
}

dependencies {
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cio)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.compression)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.resources)
    implementation(ktorLibs.server.openapi)
}
