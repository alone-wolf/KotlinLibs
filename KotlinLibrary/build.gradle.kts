plugins {
    id("java-library")
    //alias(libs.plugins.kotlinJvm)
//    kotlin("jvm") version "2.0.21"
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
    alias(ktorLibs.plugins.ktor)
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

group = "top.writerpass.libs"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation(kotlin("stdlib-jdk8"))

    implementation(ktorLibs.serialization.kotlinx.json)

    implementation(ktorLibs.client.core)
    implementation(ktorLibs.client.cio)
    implementation(ktorLibs.client.contentNegotiation)

    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cio)
    implementation(ktorLibs.server.contentNegotiation)
}
repositories {
    mavenCentral()
}
