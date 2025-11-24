package top.writerpass.micromessage.server.core.data.service.user.repository//package top.writerpass.micromessage.core.data.service.user.repository
//
//import org.jetbrains.exposed.dao.LongEntityClass
//import top.writerpass.micromessage.core.data.base.BaseRepository
//import top.writerpass.micromessage.core.data.service.user.data.User
//import top.writerpass.micromessage.core.data.service.user.entity.UserEntity
//import top.writerpass.micromessage.core.data.service.user.table.UserTable
//
//object UserRepository : BaseRepository<User, UserEntity> {
//    override val dataEntityClass: LongEntityClass<UserEntity> = UserEntity.Companion
//    override suspend fun insert(data: User): UserEntity {
//        return UserEntity.new {
//            createdAt = data.createdAt
//        }
//    }
//
//    override suspend fun update(data: User): UserEntity? {
//        return update(data.id) {
//            it.createdAt = data.createdAt
//        }
//    }
//}