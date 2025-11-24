package top.writerpass.micromessage.server.core.data.service.user.table

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import top.writerpass.micromessage.server.core.data.enums.Gender
import top.writerpass.micromessage.server.core.data.enums.Language

object UserProfileTable : IdTable<Long>() {
    override val id: Column<EntityID<Long>> = reference(
        name = "user_id",
        refColumn = UserTable.id
    ).uniqueIndex()

    val userId = id
    val nickname = varchar(
        name = "nickname",
        length = 100
    )
    val avatarUrl = varchar(
        name = "avatar_url",
        length = 100
    ).nullable()
    val gender = enumeration<Gender>("gender")
    val bio = varchar(
        name = "bio",
        length = 100
    ).nullable()
    val regionCountry = varchar(
        name = "region_country",
        length = 100
    ).nullable()
    val regionProvince = varchar(
        name = "region_province",
        length = 100
    ).nullable()
    val regionCity = varchar(
        name = "region_city",
        length = 100
    ).nullable()
    val language = enumeration<Language>("language")
    val email = varchar(
        name = "email",
        length = 100
    ).nullable()
    val phone = varchar(
        name = "phone",
        length = 100
    ).nullable()
}