package top.writerpass.micromessage.server.core.data.service.user.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import top.writerpass.micromessage.server.core.data.service.user.data.UserProfile
import top.writerpass.micromessage.server.core.data.service.user.table.UserProfileTable

class UserProfileEntity(userId: EntityID<Long>) : LongEntity(userId) {
    companion object : LongEntityClass<UserProfileEntity>(UserProfileTable)

    var userId by UserProfileTable.userId
    var nickname by UserProfileTable.nickname
    var avatarUrl by UserProfileTable.avatarUrl
    var gender by UserProfileTable.gender
    var bio by UserProfileTable.bio
    var regionCountry by UserProfileTable.regionCountry
    var regionProvince by UserProfileTable.regionProvince
    var regionCity by UserProfileTable.regionCity
    var language by UserProfileTable.language
    var email by UserProfileTable.email
    var phone by UserProfileTable.phone

    fun toData(): UserProfile {
        return UserProfile(
            userId = userId.value,
            nickname = nickname,
            avatarUrl = avatarUrl,
            gender = gender,
            bio = bio,
            regionCountry = regionCountry,
            regionProvince = regionProvince,
            regionCity = regionCity,
            language = language,
            email = email,
            phone = phone
        )
    }
}