import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.serialization)
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
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.compression)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.resources)
    implementation(ktorLibs.server.openapi)
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.callId)

    implementation(ktorLibs.serialization.kotlinx.json)

    implementation(databaseORM.bundles.exposed.sqlite)


    implementation("at.favre.lib:bcrypt:0.10.2") // 密码哈希
    implementation("ch.qos.logback:logback-classic:1.5.6") // 日志
}
