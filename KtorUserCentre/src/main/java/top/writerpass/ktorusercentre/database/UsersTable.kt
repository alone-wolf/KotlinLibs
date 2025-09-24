package top.writerpass.ktorusercentre.database

import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.jdbc.SizedIterable
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import top.writerpass.ktorusercentre.returnBadRequest
import top.writerpass.ktorusercentre.returnNotFound
import top.writerpass.ktorusercentre.returnOk

@Serializable
data class User(
    val id: Long = 0,
    val username: String,
    val email: String,
    val passwordHash1: String,
    val isActive: Boolean
) {
    companion object {
        fun new(
            username: String,
            email: String,
            passwordHash1: String,
            isActive: Boolean
        ): User {
            return User(
                username = username,
                email = email,
                passwordHash1 = passwordHash1,
                isActive = isActive
            )
        }
    }
}

object UsersTable : LongIdTable("users") {
    val username = varchar("username", 50)
    val email = varchar("email", 100).uniqueIndex()
    val passwordHash1 = varchar("password_hash1", 200)
    val isActive = bool("is_active")
}

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UsersTable)

    var username by UsersTable.username
    var email by UsersTable.email
    var passwordHash1 by UsersTable.passwordHash1
    var isActive by UsersTable.isActive

    fun toData(): User {
        return User(
            id = this.id.value,
            username = this.username,
            email = this.email,
            passwordHash1 = this.passwordHash1,
            isActive = this.isActive
        )
    }
}

object UsersDao {

    fun insert(vararg user: User) {
        user.forEach {
            insert(it)
        }
    }

    fun insert(user: User) {
        UserEntity.new {
            this.username = user.username
            this.email = user.email
            this.passwordHash1 = user.passwordHash1
            this.isActive = user.isActive
        }
    }

    fun delete(id: Long) {
        UserEntity.find { UsersTable.id eq id }.singleOrNull()?.delete()
    }

    fun deleteAll() {
        UserEntity.table.deleteAll()
    }

    fun findAllUsers(): List<UserEntity> {
        return UserEntity.all().toList()
    }

    fun findByUsername(username: String): SizedIterable<UserEntity> {
        return UserEntity.find { UsersTable.username eq username }
    }

    fun findById(id: Long): UserEntity? {
        return UserEntity.find { UsersTable.id eq id }.singleOrNull()
    }
}

fun Route.installUsersApi(path: String = "/users") = route(path) {
//    post("/inject") {
//        val users = mutableListOf<User>()
//        repeat(100) { id ->
//            val a = "${id}##${Random.nextFloat()}"
//            users.add(
//                User.new(
//                    username = a,
//                    email = a,
//                    passwordHash1 = a,
//                    isActive = true
//                )
//            )
//        }
//        transaction {
//            UsersDao.insert(*users.toTypedArray())
//        }
//        call.returnCreated("Inject success")
//    }
    get {
        val users = transaction {
            UsersDao.findAllUsers().map { it.toData() }
        }
        call.returnOk(users)
    }
    get("/{id}") {
        val userId = call.parameters["id"]?.toLongOrNull()
            ?: return@get call.returnBadRequest("Invalid user id")
        transaction {
            UsersDao.findById(userId)
        }?.let {
            call.returnOk(it.toData())
        } ?: call.returnNotFound("User not found")
    }
    delete("/{id}") {
        val userId = call.parameters["id"]?.toLongOrNull()
            ?: return@delete call.returnBadRequest("Invalid user id")
        transaction {
            UsersDao.delete(userId)
        }
        call.returnOk("Deleted")
    }
    delete {
        transaction {
            UsersDao.deleteAll()
        }
        call.returnOk("Deleted")
    }
}