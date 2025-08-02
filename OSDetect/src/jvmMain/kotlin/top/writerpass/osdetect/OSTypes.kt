package top.writerpass.osdetect

enum class OSTypes {
    WINDOWS,
    MACOS,
    LINUX,
    ANDROID,
    UNIX,     // 其他类 Unix 系统，如 BSD、AIX
    UNKNOWN;

    companion object {
        fun getCurrentOS(): OSTypes {
            val osName = System.getProperty("os.name")?.lowercase() ?: ""
            val vmName = System.getProperty("java.vm.name")?.lowercase() ?: ""
            val runtimeName = System.getProperty("java.runtime.name")?.lowercase() ?: ""

            return when {
                osName.contains("win") -> WINDOWS
                osName.contains("mac") -> MACOS
                osName.contains("nux") -> {
                    if (vmName.contains("dalvik") || runtimeName.contains("android")) {
                        ANDROID
                    } else {
                        LINUX
                    }
                }

                osName.contains("nix") || osName.contains("aix") || osName.contains("bsd") -> UNIX
                else -> UNKNOWN
            }
        }
    }
}


