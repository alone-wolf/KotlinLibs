package top.writerpass.ospaths.new1.utils

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Guid
import com.sun.jna.ptr.PointerByReference

interface Shell32 : Library {
    fun SHGetKnownFolderPath(
        rfid: Guid.GUID,
        dwFlags: Int,
        hToken: Pointer?,
        ppszPath: PointerByReference
    ): Int

    companion object {
        val INSTANCE: Shell32 = Native.load("shell32", Shell32::class.java)
    }
}