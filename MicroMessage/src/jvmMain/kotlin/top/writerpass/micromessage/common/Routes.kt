package top.writerpass.micromessage.common

open class PathNode(
    private val segment: String,
    private val parent: PathNode? = null
) {
    val path: String by lazy {
        val parentPath = parent?.path ?: ""
        if (parentPath.isEmpty()) "/$segment"
        else "$parentPath/$segment"
    }
}

open class ParamPathNode(
    segment: String,
    parent: PathNode? = null
) : PathNode(segment, parent) {

    fun param(name: String) = DynamicPathNode("{$name}", this)
}

class DynamicPathNode(
    private val segment: String,
    parent: PathNode? = null
) : PathNode(segment, parent) {

    operator fun invoke(value: String): String {
        return path.replace("{$segment}", value)
    }
}

object ServerRoutes {

    object Api : PathNode("api") {

        object V1 : PathNode("v1", Api) {

            object Auth : PathNode("auth", V1) {

                object Register : PathNode("register", Auth)
                object Login : PathNode("login", Auth)
                object Sessions : ParamPathNode("sessions", Auth) {
                    val ById = param("id")
                }
            }

            object User : PathNode("user", V1) {

                object Info : PathNode("info", User)
                object Update : PathNode("update", User)
            }
        }
    }
}
