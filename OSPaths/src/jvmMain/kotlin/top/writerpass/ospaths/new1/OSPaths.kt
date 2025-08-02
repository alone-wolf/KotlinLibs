package top.writerpass.ospaths.new1

import top.writerpass.osdetect.OSTypes

//fun main() {
//    val userDirs = OSPaths.userDirs
//    println("Home directory: ${userDirs.homeDir}")
//    println("Desktop directory: ${userDirs.desktopDir}")
//    println("Document directory: ${userDirs.documentDir}")
//    println("Download directory: ${userDirs.downloadDir}")
//    println("Music directory: ${userDirs.musicDir}")
//    println("Picture directory: ${userDirs.pictureDir}")
//    println("Video directory: ${userDirs.videoDir}")
//}
//Home directory: C:\Users\wolf
//Desktop directory: C:\Users\wolf\Desktop
//Document directory: C:\Users\wolf\Documents
//Download directory: C:\Users\wolf\Downloads
//Music directory: C:\Users\wolf\Music
//Picture directory: C:\Users\wolf\Pictures
//Video directory: C:\Users\wolf\Video

object OSPaths {
    private val currentOSType by lazy { OSTypes.getCurrentOS() }
    val userDirs: UserDir
        get() = when (currentOSType) {
            OSTypes.WINDOWS -> Windows.UserDirs
            OSTypes.LINUX -> Linux.UserDirs
            OSTypes.UNIX -> Linux.UserDirs
            OSTypes.MACOS -> MacOS.UserDirs
            else -> throw IllegalStateException("Unsupported OS type: $currentOSType")
        }

    fun getAppDirs(appName: String): AppDir {
        return when (currentOSType) {
            OSTypes.WINDOWS -> Windows.AppDirs(appName)
            OSTypes.LINUX -> Linux.AppDirs(appName)
            OSTypes.UNIX -> Linux.AppDirs(appName)
            OSTypes.MACOS -> MacOS.AppDirs(appName)
            else -> throw IllegalStateException("Unsupported OS type: $currentOSType")
        }
    }

    object Windows {
        private val userHome = System.getProperty("user.home")
//        private val appData = System.getenv("APPDATA") ?: "$userHome\\AppData\\Roaming"
//        private val localAppData = System.getenv("LOCALAPPDATA") ?: "$userHome\\AppData\\Local"

        object UserDirs : UserDir {
            override val homeDir: String = userHome
            override val desktopDir
                get() = WindowsKnownFolders.desktop ?: error("Desktop folder not found")
            override val documentDir
                get() = WindowsKnownFolders.documents ?: error("Documents folder not found")
            override val downloadDir
                get() = WindowsKnownFolders.downloads ?: error("Downloads folder not found")
            override val musicDir
                get() = WindowsKnownFolders.music ?: error("Music folder not found")
            override val pictureDir
                get() = WindowsKnownFolders.pictures ?: error("Pictures folder not found")
            override val videoDir
                get() = WindowsKnownFolders.videos ?: error("Videos folder not found")
        }

        object OSDirs : OSDir {
            override val cacheDir: String
                get() = TODO("Not yet implemented")
            override val configDir: String
                get() = TODO("Not yet implemented")
            override val dataDir: String
                get() = TODO("Not yet implemented")
            override val stateDir: String
                get() = TODO("Not yet implemented")
        }

        class AppDirs(appName: String) : AppDir {
            override val cacheDir: String
                get() = TODO("Not yet implemented")
            override val configDir: String
                get() = TODO("Not yet implemented")
            override val dataDir: String
                get() = TODO("Not yet implemented")
        }
    }

    object Linux {
        private val userHome = System.getProperty("user.home")
        private val userDirsFile = "$userHome/.config/user-dirs.dirs"

        object UserDirs : UserDir {
            override val homeDir: String = userHome
            override val desktopDir: String
                get() = TODO("Not yet implemented")
            override val documentDir: String
                get() = TODO("Not yet implemented")
            override val downloadDir: String
                get() = TODO("Not yet implemented")
            override val musicDir: String
                get() = TODO("Not yet implemented")
            override val pictureDir: String
                get() = TODO("Not yet implemented")
            override val videoDir: String
                get() = TODO("Not yet implemented")
        }

        class AppDirs(appName: String) : AppDir {
            override val cacheDir: String
                get() = TODO("Not yet implemented")
            override val configDir: String
                get() = TODO("Not yet implemented")
            override val dataDir: String
                get() = TODO("Not yet implemented")
        }
    }

    object MacOS {
        private val userHome = System.getProperty("user.home")

        private const val MACOS_DESKTOP = "Desktop"
        private const val MACOS_DOCUMENTS = "Documents"
        private const val MACOS_DOWNLOADS = "Downloads"
        private const val MACOS_MUSIC = "Music"
        private const val MACOS_PICTURES = "Pictures"
        private const val MACOS_MOVIES = "Movies"
        private const val MACOS_CACHE_DIR = "Library/Caches"
        private const val MACOS_CONFIG_DIR = "Library/Application Support"
        private const val MACOS_DATA_DIR = "Library/Application Support"

        object UserDirs : UserDir {
            override val homeDir: String = userHome
            override val desktopDir: String
                get() = "$userHome/$MACOS_DESKTOP"
            override val documentDir: String
                get() = "$userHome/$MACOS_DOCUMENTS"
            override val downloadDir: String
                get() = "$userHome/$MACOS_DOWNLOADS"
            override val musicDir: String
                get() = "$userHome/$MACOS_MUSIC"
            override val pictureDir: String
                get() = "$userHome/$MACOS_PICTURES"
            override val videoDir: String
                get() = "$userHome/$MACOS_MOVIES"
        }

        class AppDirs(private val appName: String) : AppDir {
            override val cacheDir: String
                get() = "$userHome/$MACOS_CACHE_DIR/$appName"
            override val configDir: String
                get() = "$userHome/$MACOS_CONFIG_DIR/$appName/config"
            override val dataDir: String
                get() = "$userHome/$MACOS_DATA_DIR/$appName/data"
        }
    }
}

