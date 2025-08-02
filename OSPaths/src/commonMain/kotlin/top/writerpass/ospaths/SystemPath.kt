package top.writerpass.ospaths

enum class SystemPaths {
    HOME,
    DOWNLOADS,
    DOCUMENTS,
    DESKTOP,
    PICTURES,
    MUSIC,
    VIDEOS,
    APP_DATA,       // Windows: AppData\Roaming, Linux/macOS: ~/.config
    LOCAL_APP_DATA, // Windows: AppData\Local, Linux/macOS: ~/.local/share
    CACHE           // Windows: AppData\Local\Temp, Linux/macOS: ~/.cache
}