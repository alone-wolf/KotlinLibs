package top.writerpass.rekuester.data

import io.ktor.http.HttpMethod
import top.writerpass.kmplibrary.utils.fill2Number
import top.writerpass.rekuester.data.dao.EmmListDao
import top.writerpass.rekuester.models.Api
import top.writerpass.rekuester.models.ApiHeader
import top.writerpass.rekuester.models.ApiParam
import java.util.UUID


private val builtInApis = arrayOf(
    Api(
        label = "GET All Users",
        method = HttpMethod.Get,
        address = "http://localhost:8080/api/users",
        params = listOf(
            ApiParam(
                key = "name",
                value = "value",
                description = "asdasadas",
                enabled = true
            ),
            ApiParam(
                key = "name1",
                value = "value1",
                description = "asdasadas1",
                enabled = false
            ),
        ),
        headers = listOf(
            ApiHeader(
                key = "access-token",
                value = "asdfghjkl",
                description = "Access-Token",
                enabled = true
            ),
            ApiHeader(
                key = "handwrite-header",
                value = "1q2w3e4r5t6y7u8i9o0p",
                description = "description is blank",
                enabled = false
            )

        ),
    ),
//    Api(
//        label = "GET User by Id",
//        method = HttpMethod.Get,
//        address = "http://localhost:8080/api/users/100",
//        params = emptyList(),
//        headers = emptyList(),
//        requestBody = null
//    ),
//    Api(
//        label = "Delete User by Id",
//        method = HttpMethod.Delete,
//        address = "http://localhost:8080/api/users/1",
//        params = emptyList(),
//        headers = emptyList(),
//        requestBody = null
//    ),
//    Api(
//        label = "Delete All Users",
//        method = HttpMethod.Delete,
//        address = "http://localhost:8080/api/users",
//        params = emptyList(),
//        headers = emptyList(),
//        requestBody = null
//    ),
//    Api(
//        label = "Inject 100 Users",
//        method = HttpMethod.Companion.Post,
//        address = "http://localhost:8080/api/users/inject",
//        params = emptyList(),
//        headers = emptyList(),
//        requestBody = null
//    ),
//    Api(
//        label = "Baidu",
//        method = HttpMethod.Companion.Get,
//        address = "https://baidu.com",
//        params = emptyList(),
//        headers = emptyList(),
//        requestBody = null
//    ),
)

class ApiRepository() {
    private val dao = object : EmmListDao<String, Api>() {}

    suspend fun initialLoadData() {
        repeat(10) { index ->
            val items = builtInApis.map {
                it.copy(
                    uuid = UUID.randomUUID().toString(),
                    collectionUUID = "uuid-${index.fill2Number}",
                    label = "${it.label}==${index.fill2Number}"
                )
            }.toTypedArray()
            dao.inserts(*items)
        }
        val items = builtInApis.map {
            it.copy(uuid = UUID.randomUUID().toString())
        }.toTypedArray()
        dao.inserts(*items)
    }

    val allFlow = dao.allFlow
    suspend fun findAll() = dao.findAll()
    suspend fun findById(id: String) = dao.findById(id)
    fun findByIdFlow(id: String) = dao.findByIdFlow(id)
    suspend fun delete(index: Int) = dao.delete(index)
    suspend fun delete(id: String) = dao.delete(id)
    suspend fun delete(api: Api) = dao.delete(api)

    suspend fun insert(api: Api) = dao.insert(api)
    suspend fun inserts(vararg apis: Api) = dao.inserts(*apis)
    suspend fun update(api: Api) = dao.update(api)

    suspend fun updateOrInsert(api: Api) = dao.updateOrInsert(api)

    suspend fun overWriteItems(items: List<Api>) = dao.overWriteItems(items)
}