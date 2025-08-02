package top.writerpass.ospaths.new1

import java.nio.file.Path
import java.nio.file.Paths

object XDGFolders {
    private fun parseXdgUserDirs(configFile: Path, homeDir: String): Map<String, Path> {
        return try {
            configFile.toFile().readLines()
                .filter { it.startsWith("XDG_") && it.contains('=') }
                .associate { line ->
                    val parts = line.split('=', limit = 2)
                    val key = parts[0]
                    val value = parts[1].removeSurrounding("\"").replace(
                        "$" + "HOME",
                        homeDir
                    )
                    key to Paths.get(value)
                }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}