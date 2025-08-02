package top.writerpass.ospaths.new1

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Guid
import com.sun.jna.platform.win32.Ole32
import com.sun.jna.platform.win32.ShlObj
import com.sun.jna.ptr.PointerByReference
import top.writerpass.ospaths.new1.utils.Shell32

object WindowsKnownFolders {

    val downloads: String?
        get() = getKnownFolderPath(Guids.DOWNLOADS)
    val desktop: String?
        get() = getKnownFolderPath(Guids.DESKTOP)
    val documents: String?
        get() = getKnownFolderPath(Guids.DOCUMENTS)
    val pictures: String?
        get() = getKnownFolderPath(Guids.PICTURES)
    val music: String?
        get() = getKnownFolderPath(Guids.MUSIC)
    val videos: String?
        get() = getKnownFolderPath(Guids.VIDEOS)
    val localAppData: String?
        get() = getKnownFolderPath(Guids.LOCAL_APP_DATA)
    val roamingAppData: String?
        get() = getKnownFolderPath(Guids.ROAMING_APP_DATA)
    val programData: String?
        get() = getKnownFolderPath(Guids.PROGRAM_DATA)
    val savedGames: String?
        get() = getKnownFolderPath(Guids.SAVED_GAMES)
    val quickLaunch: String?
        get() = getKnownFolderPath(Guids.QUICK_LAUNCH)
    val templates: String?
        get() = getKnownFolderPath(Guids.TEMPLATES)
    val favorites: String?
        get() = getKnownFolderPath(Guids.FAVORITES)
    val internetCache: String?
        get() = getKnownFolderPath(Guids.INTERNET_CACHE)
    val cookies: String?
        get() = getKnownFolderPath(Guids.COOKIES)

    object Guids {
        const val DOWNLOADS = "{374DE290-123F-4565-9164-39C4925E467B}"
        const val DESKTOP = "{B4BFCC3A-DB2C-424C-B029-7FE99A87C641}"
        const val DOCUMENTS = "{FDD39AD0-238F-46AF-ADB4-6C85480369C7}"
        const val PICTURES = "{33E28130-4E1E-4676-835A-98395C3BC3BB}"
        const val MUSIC = "{4BD8D571-6D19-48D3-BE97-422220080E43}"
        const val VIDEOS = "{18989B1D-99B5-455B-841C-AB7C74E4DDFC}"
        const val LOCAL_APP_DATA = "{F1B32785-6FBA-4FCF-9D55-7B8E7F157091}"
        const val ROAMING_APP_DATA = "{3EB685DB-65F9-4CF6-A03A-E3EF65729F3D}"
        const val PROGRAM_DATA = "{62AB5D82-FDC1-4DC3-A9DD-070D1D495D97}"
        const val SAVED_GAMES = "{4C5C32FF-BB9D-43B0-B5B4-2D72E54EAAA4}"
        const val QUICK_LAUNCH = "{52A4F021-7B75-48A9-9F6B-4B87A210BC8F}"
        const val TEMPLATES = "{A63293E8-664E-48DB-A079-DF759E0509F7}"
        const val FAVORITES = "{1777F761-68AD-4D8A-87BD-30B759FA33DD}"
        const val INTERNET_CACHE = "{352481E8-33BE-4251-BA85-6007CAEDCF9D}"
        const val COOKIES = "{2B0F765D-C0E9-4171-908E-08A611B84FF6}"
    }

    /**
     * 获取指定 Known Folder GUID 对应的路径
     * @param folderIdGuid Known Folder 的 GUID 字符串（如 Downloads）
     * @return 该目录的完整路径（如 D:\MyDownloads），如果失败则返回 null
     */
    fun getKnownFolderPath(folderIdGuid: String): String? {
        val folderId = Guid.GUID(folderIdGuid)
        val outPath = PointerByReference()

        val hResult = Shell32.INSTANCE.SHGetKnownFolderPath(folderId, 0, null, outPath)
        return if (hResult == 0 && outPath.value != Pointer.NULL) {
            try {
                outPath.value.getWideString(0)
            } finally {
                Ole32.INSTANCE.CoTaskMemFree(outPath.value)
            }
        } else {
            null
        }
    }
}


//Downloads: C:\Users\wolf\Downloads
//Desktop: C:\Users\wolf\Desktop
//Documents: C:\Users\wolf\Documents
//Pictures: C:\Users\wolf\Pictures
//Music: C:\Users\wolf\Music
//Videos: C:\Users\wolf\v
//LocalAppData: C:\Users\wolf\AppData\Local
//RoamingAppData: C:\Users\wolf\AppData\Roaming
//ProgramData: C:\ProgramData
//SavedGames: C:\Users\wolf\Saved Games
//QuickLaunch: C:\Users\wolf\AppData\Roaming\Microsoft\Internet Explorer\Quick Launch
//Templates: C:\Users\wolf\AppData\Roaming\Microsoft\Windows\Templates
//Favorites: C:\Users\wolf\Favorites
//InternetCache: C:\Users\wolf\AppData\Local\Microsoft\Windows\INetCache
//Cookies: C:\Users\wolf\AppData\Local\Microsoft\Windows\INetCookies

//fun main() {
//    WindowsKnownFolders.also {
//        println("Downloads: ${it.downloads}")
//        println("Desktop: ${it.desktop}")
//        println("Documents: ${it.documents}")
//        println("Pictures: ${it.pictures}")
//        println("Music: ${it.music}")
//        println("Videos: ${it.videos}")
//        println("LocalAppData: ${it.localAppData}")
//        println("RoamingAppData: ${it.roamingAppData}")
//        println("ProgramData: ${it.programData}")
//        println("SavedGames: ${it.savedGames}")
//        println("QuickLaunch: ${it.quickLaunch}")
//        println("Templates: ${it.templates}")
//        println("Favorites: ${it.favorites}")
//        println("InternetCache: ${it.internetCache}")
//        println("Cookies: ${it.cookies}")
//    }
//}