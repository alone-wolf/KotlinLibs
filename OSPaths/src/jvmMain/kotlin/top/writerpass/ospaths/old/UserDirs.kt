//package top.writerpass.ospaths.old
//
//import java.nio.file.Path
//import java.nio.file.Paths
//
///**
// * User directories for common user content locations.
// * Provides cross-platform access to standard user directories.
// */
//data class UserDirs(
//    val desktopDir: Path,
//    val documentDir: Path,
//    val downloadDir: Path,
//    val musicDir: Path,
//    val pictureDir: Path,
//    val publicDir: Path,
//    val videoDir: Path
//) {
//    companion object {
//        private const val WINDOWS_DESKTOP = "DESKTOP"
//        private const val WINDOWS_DOCUMENTS = "PERSONAL"
//        private const val WINDOWS_DOWNLOADS = "DOWNLOADS"
//        private const val WINDOWS_MUSIC = "MYMUSIC"
//        private const val WINDOWS_PICTURES = "MYPICTURES"
//        private const val WINDOWS_VIDEOS = "MYVIDEO"
//
//        private const val MACOS_DESKTOP = "Desktop"
//        private const val MACOS_DOCUMENTS = "Documents"
//        private const val MACOS_DOWNLOADS = "Downloads"
//        private const val MACOS_MUSIC = "Music"
//        private const val MACOS_PICTURES = "Pictures"
//        private const val MACOS_PUBLIC = "Public"
//        private const val MACOS_MOVIES = "Movies"
//
//        private const val UNIX_DESKTOP = "Desktop"
//        private const val UNIX_DOCUMENTS = "Documents"
//        private const val UNIX_DOWNLOADS = "Downloads"
//        private const val UNIX_MUSIC = "Music"
//        private const val UNIX_PICTURES = "Pictures"
//        private const val UNIX_PUBLIC = "Public"
//        private const val UNIX_VIDEOS = "Videos"
//
//        /**
//         * Create UserDirs instance with platform-specific directory locations.
//         * @return UserDirs instance or null if home directory cannot be determined
//         */
//        fun create(): UserDirs? {
//            val homeDir = System.getProperty("user.home")?.let { Paths.get(it) } ?: return null
//            val osName = System.getProperty("os.name").lowercase()
//
//            return when {
//                osName.contains("win") -> createWindowsUserDirs(homeDir)
//                osName.contains("mac") -> createMacOSUserDirs(homeDir)
//                else -> createUnixUserDirs(homeDir)
//            }
//        }
//
//        private fun createWindowsUserDirs(homeDir: Path): UserDirs {
//            val userProfile = System.getenv("USERPROFILE") ?: homeDir.toString()
//            val userProfilePath = Paths.get(userProfile)
//
//            return UserDirs(
//                desktopDir = getWindowsKnownFolder("{B4BFCC3A-DB2C-424C-B029-7FE99A87C641}")
//                    ?: userProfilePath.resolve("Desktop"),
//                documentDir = getWindowsKnownFolder("{FDD39AD0-238F-46AF-ADB4-6C85480369C7}")
//                    ?: userProfilePath.resolve("Documents"),
//                downloadDir = getWindowsKnownFolder("{374DE290-123F-4565-9164-39C4925E467B}")
//                    ?: userProfilePath.resolve("Downloads"),
//                musicDir = getWindowsKnownFolder("{4BD8D571-6D19-48D3-BE97-422220080E43}")
//                    ?: userProfilePath.resolve("Music"),
//                pictureDir = getWindowsKnownFolder("{33E28130-4E1E-4676-835A-98395C3BC3BB}")
//                    ?: userProfilePath.resolve("Pictures"),
//                publicDir = getWindowsKnownFolder("{DFDF76A2-C82A-4D63-906A-5644AC457385}")
//                    ?: userProfilePath.resolve("Public"),
//                videoDir = getWindowsKnownFolder("{18989B1D-99B5-455B-841C-AB7C74E4DDFC}")
//                    ?: userProfilePath.resolve("Videos")
//            )
//        }
//
//        private fun createMacOSUserDirs(homeDir: Path): UserDirs {
//            return UserDirs(
//                desktopDir = homeDir.resolve(MACOS_DESKTOP),
//                documentDir = homeDir.resolve(MACOS_DOCUMENTS),
//                downloadDir = homeDir.resolve(MACOS_DOWNLOADS),
//                musicDir = homeDir.resolve(MACOS_MUSIC),
//                pictureDir = homeDir.resolve(MACOS_PICTURES),
//                publicDir = homeDir.resolve(MACOS_PUBLIC),
//                videoDir = homeDir.resolve(MACOS_MOVIES)
//            )
//        }
//
//        private fun createUnixUserDirs(homeDir: Path): UserDirs {
//            val xdgDirs = mapOf(
//                "XDG_DESKTOP_DIR" to UNIX_DESKTOP,
//                "XDG_DOCUMENTS_DIR" to UNIX_DOCUMENTS,
//                "XDG_DOWNLOAD_DIR" to UNIX_DOWNLOADS,
//                "XDG_MUSIC_DIR" to UNIX_MUSIC,
//                "XDG_PICTURES_DIR" to UNIX_PICTURES,
//                "XDG_PUBLICSHARE_DIR" to UNIX_PUBLIC,
//                "XDG_VIDEOS_DIR" to UNIX_VIDEOS
//            )
//
//            val userDirsFile = homeDir.resolve(".config/user-dirs.dirs")
//            val xdgConfig = if (userDirsFile.toFile().exists()) {
//                parseXdgUserDirs(userDirsFile)
//            } else {
//                emptyMap()
//            }
//
//            return UserDirs(
//                desktopDir = xdgConfig["XDG_DESKTOP_DIR"] ?: homeDir.resolve(UNIX_DESKTOP),
//                documentDir = xdgConfig["XDG_DOCUMENTS_DIR"] ?: homeDir.resolve(UNIX_DOCUMENTS),
//                downloadDir = xdgConfig["XDG_DOWNLOAD_DIR"] ?: homeDir.resolve(UNIX_DOWNLOADS),
//                musicDir = xdgConfig["XDG_MUSIC_DIR"] ?: homeDir.resolve(UNIX_MUSIC),
//                pictureDir = xdgConfig["XDG_PICTURES_DIR"] ?: homeDir.resolve(UNIX_PICTURES),
//                publicDir = xdgConfig["XDG_PUBLICSHARE_DIR"] ?: homeDir.resolve(UNIX_PUBLIC),
//                videoDir = xdgConfig["XDG_VIDEOS_DIR"] ?: homeDir.resolve(UNIX_VIDEOS)
//            )
//        }
//
//        private fun getWindowsKnownFolder(guid: String): Path? {
//            return try {
//                val process = ProcessBuilder(
//                    "powershell",
//                    "-Command",
//                    "[Environment]::GetFolderPath([Environment+SpecialFolder]::$guid)"
//                ).start()
//
//                val path = process.inputStream.bufferedReader().readLine()?.trim()
//                if (path.isNullOrEmpty()) null else Paths.get(path)
//            } catch (e: Exception) {
//                null
//            }
//        }
//
//        private fun parseXdgUserDirs(configFile: Path): Map<String, Path> {
//            return try {
//                configFile.toFile().readLines()
//                    .filter { it.startsWith("XDG_") && it.contains('=') }
//                    .associate { line ->
//                        val parts = line.split('=', limit = 2)
//                        val key = parts[0]
//                        val value = parts[1].removeSurrounding("\"").replace(
//                            "$" + "HOME",
//                            System.getProperty("user.home") ?: ""
//                        )
//                        key to Paths.get(value)
//                    }
//            } catch (e: Exception) {
//                emptyMap()
//            }
//        }
//    }
//
//    /**
//     * Ensure all user directories exist, creating them if necessary.
//     */
//    fun ensureDirectories(): Boolean {
//        return listOf(
//            desktopDir,
//            documentDir,
//            downloadDir,
//            musicDir,
//            pictureDir,
//            publicDir,
//            videoDir
//        ).all { dir ->
//            try {
//                dir.toFile().mkdirs()
//            } catch (e: SecurityException) {
//                false
//            }
//        }
//    }
//}