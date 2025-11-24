package top.writerpass.micromessage.server.core.data.service.user.repository//package top.writerpass.micromessage.core.data.service.user.repository
//
//import org.jetbrains.exposed.dao.LongEntityClass
//import top.writerpass.micromessage.core.data.base.BaseRepository
//import top.writerpass.micromessage.core.data.service.user.data.UserProfile
//import top.writerpass.micromessage.core.data.service.user.entity.UserProfileEntity
//
//object UserProfileRepository : BaseRepository<UserProfile, UserProfileEntity> {
//    override val dataEntityClass: LongEntityClass<UserProfileEntity> = UserProfileEntity.Companion
//    override suspend fun insert(data: UserProfile): UserProfileEntity {
//        return UserProfileEntity.new(data.userId) {
//            nickname = data.nickname
//            avatarUrl = data.avatarUrl
//            gender = data.gender
//            bio = data.bio
//            regionCountry = data.regionCountry
//            regionCity = data.regionCity
//            language = data.language
//            email = data.email
//            phone = data.phone
//        }
//    }
//
//    override suspend fun update(data: UserProfile): UserProfileEntity? {
//        return update(data.id){
//            it.nickname = data.nickname
//            it.avatarUrl = data.avatarUrl
//            it.gender = data.gender
//            it.bio = data.bio
//            it.regionCountry = data.regionCountry
//            it.regionCity = data.regionCity
//            it.language = data.language
//            it.email = data.email
//            it.phone = data.phone
//        }
//    }
//}