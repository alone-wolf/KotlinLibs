//package top.writerpass.ospaths.old
//
//import java.nio.file.Path
//import java.nio.file.Paths
//
///**
// * Application directories following platform conventions.
// * Based on XDG Base Directory Specification for Unix-like systems,
// * with platform-specific fallbacks for Windows and macOS.
// */
//data class AppDirs(
//    val cacheDir: Path,
//    val configDir: Path,
//    val dataDir: Path,
//    val stateDir: Path
//) {
//    companion object {
//        private const val XDG_CACHE_HOME = "XDG_CACHE_HOME"
//        private const val XDG_CONFIG_HOME = "XDG_CONFIG_HOME"
//        private const val XDG_DATA_HOME = "XDG_DATA_HOME"
//        private const val XDG_STATE_HOME = "XDG_STATE_HOME"
//
//        private const val WINDOWS_APP_DATA = "APPDATA"
//        private const val WINDOWS_LOCAL_APP_DATA = "LOCALAPPDATA"
//
//        private const val MACOS_CACHE_DIR = "Library/Caches"
//        private const val MACOS_CONFIG_DIR = "Library/Application Support"
//        private const val MACOS_DATA_DIR = "Library/Application Support"
//
//        /**
//         * Create AppDirs for the given application name.
//         * @param appName Application name used as subdirectory name
//         * @param useXdgOnMacOS Whether to use XDG conventions on macOS instead of native macOS conventions
//         * @return AppDirs instance or null if home directory cannot be determined
//         */
//        fun create(appName: String? = null, useXdgOnMacOS: Boolean = false): AppDirs? {
//            val osName = System.getProperty("os.name").lowercase()
//            val homeDir = System.getProperty("user.home")?.let { Paths.get(it) } ?: return null
//
//            return when {
//                osName.contains("mac") && !useXdgOnMacOS -> createMacOSAppDirs(homeDir, appName)
//                osName.contains("win") -> createWindowsAppDirs(appName)
//                else -> createUnixAppDirs(homeDir, appName)
//            }
//        }
//
//        private fun createMacOSAppDirs(homeDir: Path, appName: String?): AppDirs {
//            val baseCacheDir = homeDir.resolve(MACOS_CACHE_DIR)
//            val baseConfigDir = homeDir.resolve(MACOS_CONFIG_DIR)
//            val baseDataDir = homeDir.resolve(MACOS_DATA_DIR)
//
//            val cacheDir = if (appName != null) baseCacheDir.resolve(appName) else baseCacheDir
//            val configDir = if (appName != null) baseConfigDir.resolve(appName) else baseConfigDir
//            val dataDir = if (appName != null) baseDataDir.resolve(appName) else baseDataDir
//            val stateDir = dataDir
//
//            return AppDirs(cacheDir, configDir, dataDir, stateDir)
//        }
//
//        private fun createWindowsAppDirs(appName: String?): AppDirs? {
//            val appData = System.getenv(WINDOWS_APP_DATA) ?: return null
//            val localAppData = System.getenv(WINDOWS_LOCAL_APP_DATA) ?: return null
//
//            val baseConfigDir = Paths.get(appData)
//            val baseCacheDir = Paths.get(localAppData)
//            val baseDataDir = Paths.get(localAppData)
//
//            val cacheDir = if (appName != null) baseCacheDir.resolve(appName) else baseCacheDir
//            val configDir = if (appName != null) baseConfigDir.resolve(appName) else baseConfigDir
//            val dataDir = if (appName != null) baseDataDir.resolve(appName) else baseDataDir
//            val stateDir = dataDir
//
//            return AppDirs(cacheDir, configDir, dataDir, stateDir)
//        }
//
//        private fun createUnixAppDirs(homeDir: Path, appName: String?): AppDirs {
//            val baseCacheDir = getEnvPath(XDG_CACHE_HOME) ?: homeDir.resolve(".cache")
//            val baseConfigDir = getEnvPath(XDG_CONFIG_HOME) ?: homeDir.resolve(".config")
//            val baseDataDir = getEnvPath(XDG_DATA_HOME) ?: homeDir.resolve(".local/share")
//            val baseStateDir = getEnvPath(XDG_STATE_HOME) ?: homeDir.resolve(".local/state")
//
//            val cacheDir = if (appName != null) baseCacheDir.resolve(appName) else baseCacheDir
//            val configDir = if (appName != null) baseConfigDir.resolve(appName) else baseConfigDir
//            val dataDir = if (appName != null) baseDataDir.resolve(appName) else baseDataDir
//            val stateDir = if (appName != null) baseStateDir.resolve(appName) else baseStateDir
//
//            return AppDirs(cacheDir, configDir, dataDir, stateDir)
//        }
//
//        private fun getEnvPath(envVar: String): Path? {
//            val pathStr = System.getenv(envVar) ?: return null
//            val path = Paths.get(pathStr)
//            return if (path.isAbsolute) path else null
//        }
//    }
//
//    /**
//     * Ensure all directories exist, creating them if necessary.
//     */
//    fun ensureDirectories(): Boolean {
//        return listOf(cacheDir, configDir, dataDir, stateDir).all { dir ->
//            try {
//                dir.toFile().mkdirs()
//            } catch (e: SecurityException) {
//                false
//            }
//        }
//    }
//}