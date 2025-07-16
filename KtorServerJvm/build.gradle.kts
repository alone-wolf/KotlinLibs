plugins {
    alias(libs.plugins.multiplatform)
    alias(ktorLibs.plugins.ktor)
}

group = "top.writerpass.libs"
version = "1.0.0"

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {}
        jvmMain.dependencies {
            api(ktorLibs.server.core)
            api(ktorLibs.server.cio)
            api(ktorLibs.server.cors)
            api(ktorLibs.server.sse)
            api(ktorLibs.server.contentNegotiation)
            api(ktorLibs.server.websockets)
            api(ktorLibs.server.callLogging)
            api(ktorLibs.server.auth)
            api(ktorLibs.server.defaultHeaders)
            api(ktorLibs.server.resources)
            api(ktorLibs.server.statusPages)
            api(ktorLibs.server.compression)


            implementation(ktorLibs.serialization.kotlinx.json)
        }
    }
}
