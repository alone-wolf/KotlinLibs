package top.writerpass.ospaths

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Utility functions for path manipulation and validation.
 */
object PathUtils {
    
    /**
     * Check if a path is absolute.
     * @param path Path to check
     * @return true if the path is absolute, false otherwise
     */
    fun isAbsolutePath(path: String): Boolean {
        return Paths.get(path).isAbsolute
    }
    
    /**
     * Check if a path is absolute.
     * @param path Path to check
     * @return true if the path is absolute, false otherwise
     */
    fun isAbsolutePath(path: Path): Boolean {
        return path.isAbsolute
    }
    
    /**
     * Resolve a path relative to the user's home directory.
     * @param relativePath Relative path to resolve
     * @return Resolved absolute path
     */
    fun resolveHomePath(relativePath: String): Path {
        val homeDir = System.getProperty("user.home") ?: "."
        return Paths.get(homeDir).resolve(relativePath)
    }
    
    /**
     * Get the current working directory.
     * @return Current working directory as Path
     */
    fun getCurrentWorkingDirectory(): Path {
        return Paths.get(System.getProperty("user.dir") ?: ".")
    }
    
    /**
     * Get the temporary directory for the current system.
     * @return System temporary directory as Path
     */
    fun getTempDirectory(): Path {
        return Paths.get(System.getProperty("java.io.tmpdir") ?: "/tmp")
    }
    
    /**
     * Ensure a directory exists, creating it if necessary.
     * @param path Directory path to ensure
     * @return true if directory exists or was created successfully, false otherwise
     */
    fun ensureDirectory(path: Path): Boolean {
        return try {
            if (!path.toFile().exists()) {
                path.toFile().mkdirs()
            } else {
                path.toFile().isDirectory
            }
        } catch (e: SecurityException) {
            false
        }
    }
    
    /**
     * Ensure a directory exists, creating it if necessary.
     * @param path Directory path as string to ensure
     * @return true if directory exists or was created successfully, false otherwise
     */
    fun ensureDirectory(path: String): Boolean {
        return ensureDirectory(Paths.get(path))
    }
    
    /**
     * Check if a file or directory exists and is readable.
     * @param path Path to check
     * @return true if path exists and is readable, false otherwise
     */
    fun isReadable(path: Path): Boolean {
        return path.toFile().let { it.exists() && it.canRead() }
    }
    
    /**
     * Check if a file or directory exists and is writable.
     * @param path Path to check
     * @return true if path exists and is writable, false otherwise
     */
    fun isWritable(path: Path): Boolean {
        return path.toFile().let { it.exists() && it.canWrite() }
    }
    
    /**
     * Check if a path exists and is executable.
     * @param path Path to check
     * @return true if path exists and is executable, false otherwise
     */
    fun isExecutable(path: Path): Boolean {
        return path.toFile().let { it.exists() && it.canExecute() }
    }
    
    /**
     * Get the file extension from a path.
     * @param path Path to extract extension from
     * @return File extension (without dot) or empty string if no extension
     */
    fun getFileExtension(path: Path): String {
        val fileName = path.fileName?.toString() ?: ""
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot > 0) fileName.substring(lastDot + 1) else ""
    }
    
    /**
     * Get the file name without extension from a path.
     * @param path Path to extract name from
     * @return File name without extension
     */
    fun getFileNameWithoutExtension(path: Path): String {
        val fileName = path.fileName?.toString() ?: ""
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot > 0) fileName.substring(0, lastDot) else fileName
    }
    
    /**
     * Normalize a path by resolving . and .. components.
     * @param path Path to normalize
     * @return Normalized path
     */
    fun normalizePath(path: String): Path {
        return Paths.get(path).normalize()
    }
    
    /**
     * Join multiple path components together.
     * @param components Path components to join
     * @return Joined path
     */
    fun joinPaths(vararg components: String): Path {
        if (components.isEmpty()) return Paths.get("")
        
        var result = Paths.get(components[0])
        for (i in 1 until components.size) {
            result = result.resolve(components[i])
        }
        return result
    }
    
    /**
     * Get the relative path between two paths.
     * @param from Source path
     * @param to Target path
     * @return Relative path from source to target
     */
    fun getRelativePath(from: Path, to: Path): Path {
        return from.relativize(to)
    }
    
    /**
     * Get the relative path between two paths.
     * @param from Source path as string
     * @param to Target path as string
     * @return Relative path from source to target
     */
    fun getRelativePath(from: String, to: String): Path {
        return getRelativePath(Paths.get(from), Paths.get(to))
    }
    
    /**
     * Check if a path is a symbolic link.
     * @param path Path to check
     * @return true if path is a symbolic link, false otherwise
     */
    fun isSymbolicLink(path: Path): Boolean {
        return try {
            Files.isSymbolicLink(path)
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get the canonical (absolute and normalized) form of a path.
     * @param path Path to canonicalize
     * @return Canonical path
     */
    fun getCanonicalPath(path: Path): Path {
        return try {
            path.toFile().canonicalFile.toPath()
        } catch (e: Exception) {
            path.toAbsolutePath().normalize()
        }
    }
    
    /**
     * Get the file size in bytes.
     * @param path Path to the file
     * @return File size in bytes, or -1 if file doesn't exist or is not a file
     */
    fun getFileSize(path: Path): Long {
        return try {
            if (path.toFile().isFile) path.toFile().length() else -1
        } catch (e: Exception) {
            -1
        }
    }
    
    /**
     * List all files in a directory.
     * @param directory Directory to list
     * @return List of files in the directory, or empty list if directory doesn't exist
     */
    fun listFiles(directory: Path): List<Path> {
        return directory.toFile().listFiles()?.map { it.toPath() } ?: emptyList()
    }
    
    /**
     * List all directories in a directory.
     * @param directory Directory to list
     * @return List of directories in the directory, or empty list if directory doesn't exist
     */
    fun listDirectories(directory: Path): List<Path> {
        return directory.toFile().listFiles { file -> file.isDirectory }?.map { it.toPath() } ?: emptyList()
    }
}