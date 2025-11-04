import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.serialization)
}


kotlin {
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.androidx.navigation.compose)
                implementation(libs.androidx.lifecycle.viewmodel)
//                api("io.github.dautovicharis:charts:2.0.0")

                implementation(project(":KMPLibrary"))
                implementation(project(":CMPLibrary"))

                implementation("co.touchlab:kermit:2.0.8")

                implementation(ktorLibs.client.core)
                implementation(ktorLibs.client.cio)
                implementation(ktorLibs.client.contentNegotiation)
                implementation(ktorLibs.client.logging)
                implementation(ktorLibs.client.encoding)
                implementation(ktorLibs.client.auth)

                implementation(ktorLibs.serialization.kotlinx.json)
//                implementation("com.darkrockstudios:mpfilepicker:3.1.0")

//                implementation(databaseORM.bundles.exposed.sqlite)

//                implementation("com.github.pingpongboss:compose-exploded-layers:1.0.7")
            }

        }
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.kstore.file)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "top.writerpass.rekuester.RekuesterKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Rekuester"
            packageVersion = "1.0.4"
//            includeAllModules = true

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
                dirChooser = true
                menu = true
                shortcut = true
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/MacosIcon.icns"))
                bundleID = "top.writerpass.rekuester"
                dockName = "Rekuester"
                setDockNameSameAsPackageName = true
            }
        }
    }
}


//tasks.register("generateDependencyList") {
//    group = "build"
//    description = "生成依赖列表并写入 resources"
//
//    val outputDir = layout.buildDirectory.dir("generated/resources/dependencies")
//
//    outputs.dir(outputDir)
//
//    doLast {
//        val runtimeConfig = configurations.getByName("classpath")
//
//        val deps = runtimeConfig
//            .resolvedConfiguration
//            .resolvedArtifacts
//            .map { artifact ->
//                val id = artifact.moduleVersion.id
//                "${id.group}:${artifact.name}:${id.version}"
//            }
//            .sorted()
//            .joinToString("\n")
//
//        val outputFile = outputDir.get().file("dependencies.txt").asFile
//        outputFile.parentFile.mkdirs()
//        outputFile.writeText(deps)
//
//        println("✅ 依赖列表已写入: ${outputFile.absolutePath}")
//    }
//}