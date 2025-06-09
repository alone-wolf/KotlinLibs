package top.writerpass.ktorlibrary.file_slice_send.server

import io.ktor.util.collections.ConcurrentMap
import top.writerpass.kotlinlibrary.utils.switch
import java.util.UUID

// skip multi-thread security
object UploadCacheStore {
    private val uploadCaches = ConcurrentMap<String, UploadCache>()
    fun getAllCaches() = uploadCaches.values.toList()

    fun new(
        name: String,
        size: Long,
        hash: String?,
        fragmentFullNum: Int,
        fragmentFullSize: Long,
        fragmentLastSize: Long
    ): UploadCache {
        val uuid = UUID.randomUUID().toString()
        val cache = UploadCache(
            sessionId = uuid,
            name = name,
            size = size,
            hash = hash,
            fragmentFullNum = fragmentFullNum,
            fragmentFullSize = fragmentFullSize,
            fragmentLastSize = fragmentLastSize
        )
        uploadCaches[uuid] = cache
        return cache
    }

    operator fun get(sessionId: String): UploadCache? {
        return uploadCaches[sessionId]
    }

    fun update(sessionId: String, updateBlock: (UploadCache) -> UploadCache) {
        uploadCaches[sessionId].switch {
            val updated = updateBlock(it)
            uploadCaches.replace(sessionId, it, updated)
        }
        val current = uploadCaches[sessionId] ?: return
    }
}