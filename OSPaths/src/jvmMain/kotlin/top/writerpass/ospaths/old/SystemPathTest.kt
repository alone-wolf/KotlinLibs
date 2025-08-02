//package top.writerpass.ospaths.old
//
///**
// * Simple test utility for the system path library.
// */
//internal object SystemPathTest {
//
//    @JvmStatic
//    fun main(args: Array<String>) {
//        println("=== System Path Library Test ===")
//        println("Operating System: ${System.getProperty("os.name")}")
//        println("User Home: ${System.getProperty("user.home")}")
//        println()
//
//        // Test AppDirs
//        println("--- Application Directories ---")
//        val appDirs = AppDirs.create("MyApp")
//        if (appDirs != null) {
//            println("Cache Directory: ${appDirs.cacheDir}")
//            println("Config Directory: ${appDirs.configDir}")
//            println("Data Directory: ${appDirs.dataDir}")
//            println("State Directory: ${appDirs.stateDir}")
//
//            println("\nCreating directories...")
//            val success = appDirs.ensureDirectories()
//            println("Directories created: $success")
//        } else {
//            println("Failed to determine application directories")
//        }
//
//        println()
//
//        // Test UserDirs
//        println("--- User Directories ---")
//        val userDirs = UserDirs.create()
//        if (userDirs != null) {
//            println("Desktop: ${userDirs.desktopDir}")
//            println("Documents: ${userDirs.documentDir}")
//            println("Downloads: ${userDirs.downloadDir}")
//            println("Music: ${userDirs.musicDir}")
//            println("Pictures: ${userDirs.pictureDir}")
//            println("Public: ${userDirs.publicDir}")
//            println("Videos: ${userDirs.videoDir}")
//        } else {
//            println("Failed to determine user directories")
//        }
//
//        println()
//
//        // Test PathUtils
//        println("--- Path Utils ---")
//        println("Current Working Directory: ${PathUtils.getCurrentWorkingDirectory()}")
//        println("Temporary Directory: ${PathUtils.getTempDirectory()}")
//        println("Is absolute '/tmp/test': ${PathUtils.isAbsolutePath("/tmp/test")}")
//        println("Home path resolved: ${PathUtils.resolveHomePath("test/path")}")
//
//        // Test directory creation
//        val testDir = PathUtils.getTempDirectory().resolve("system_path_test")
//        println("Creating test directory: $testDir")
//        val created = PathUtils.ensureDirectory(testDir)
//        println("Directory created: $created")
//
//        println("\n=== Test Complete ===")
//    }
//}