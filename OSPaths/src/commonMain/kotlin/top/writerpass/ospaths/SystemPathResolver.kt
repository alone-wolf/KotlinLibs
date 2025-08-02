package top.writerpass.ospaths

import java.io.File
import java.util.Locale

object SystemPathResolver {

    private val os: OSType = detectOS()
    private val userHome: String = System.getProperty("user.home") ?: error("Failed to get user home directory")

    fun resolve(path: SystemPaths): File {
        return when (os) {
            OSType.WINDOWS -> resolveWindows(path)
            OSType.MAC -> resolveMac(path)
            OSType.LINUX -> resolveLinux(path)
            OSType.UNKNOWN -> throw UnsupportedOperationException("Unsupported operating system: ${System.getProperty("os.name")}")
        }
    }

    private fun detectOS(): OSType {
        val osName = System.getProperty("os.name").lowercase(Locale.ENGLISH)
        return when {
            osName.contains("win") -> OSType.WINDOWS
            osName.contains("mac") -> OSType.MAC
            osName.contains("nux") || osName.contains("nix") -> OSType.LINUX
            else -> OSType.UNKNOWN
        }
    }

    private fun resolveWindows(path: SystemPaths): File {
        val userProfile = System.getenv("USERPROFILE") ?: userHome
        val appData = System.getenv("APPDATA") ?: "$userProfile\\AppData\\Roaming"
        val localAppData = System.getenv("LOCALAPPDATA") ?: "$userProfile\\AppData\\Local"
        val downloads = "$userProfile\\Downloads"

        return when (path) {
            SystemPaths.HOME -> File(userProfile)
            SystemPaths.DESKTOP -> File(userProfile, "Desktop")
            SystemPaths.DOCUMENTS -> File(userProfile, "Documents")
            SystemPaths.DOWNLOADS -> File(downloads)
            SystemPaths.PICTURES -> File(userProfile, "Pictures")
            SystemPaths.MUSIC -> File(userProfile, "Music")
            SystemPaths.VIDEOS -> File(userProfile, "Videos")
            SystemPaths.APP_DATA -> File(appData)
            SystemPaths.LOCAL_APP_DATA -> File(localAppData)
            SystemPaths.CACHE -> File(localAppData, "Temp")
        }
    }

    private fun resolveMac(path: SystemPaths): File {
        val downloads = "$userHome/Downloads"
        return when (path) {
            SystemPaths.HOME -> File(userHome)
            SystemPaths.DESKTOP -> File(userHome, "Desktop")
            SystemPaths.DOCUMENTS -> File(userHome, "Documents")
            SystemPaths.DOWNLOADS -> File(downloads)
            SystemPaths.PICTURES -> File(userHome, "Pictures")
            SystemPaths.MUSIC -> File(userHome, "Music")
            SystemPaths.VIDEOS -> File(userHome, "Movies") // macOS default videos directory
            SystemPaths.APP_DATA -> File(userHome, "Library/Application Support")
            SystemPaths.LOCAL_APP_DATA -> File(userHome, "Library/Application Support")
            SystemPaths.CACHE -> File(userHome, "Library/Caches")
        }
    }

    private fun resolveLinux(path: SystemPaths): File {
        return when (path) {
            SystemPaths.HOME -> File(userHome)
            SystemPaths.DESKTOP -> File(userHome, "Desktop")
            SystemPaths.DOCUMENTS -> File(userHome, "Documents")
            SystemPaths.DOWNLOADS -> File(userHome, "Downloads")
            SystemPaths.PICTURES -> File(userHome, "Pictures")
            SystemPaths.MUSIC -> File(userHome, "Music")
            SystemPaths.VIDEOS -> File(userHome, "Videos")
            SystemPaths.APP_DATA -> File(userHome, ".config")
            SystemPaths.LOCAL_APP_DATA -> File(userHome, ".local/share")
            SystemPaths.CACHE -> File(userHome, ".cache")
        }
    }
}
