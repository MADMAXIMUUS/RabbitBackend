package com.rabbit.server.profile.data.models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserTable : LongIdTable("user", "id") {

    val name = varchar("name", 256)
    val username = varchar("username", 256).uniqueIndex().nullable()
    val email = varchar("email", 256).uniqueIndex()
    val passwordSalt = varchar("password_salt", 256)
    val passwordHash = varchar("password_hash", 256)
    val avatar = varchar("avatar_url", 256).nullable()
    val birthdate = date("birthdate")
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
}

class UserEntity(id: EntityID<Long>) : LongEntity(id) {

    var name by UserTable.name
    var username by UserTable.username
    var email by UserTable.email
    var passwordHash by UserTable.passwordHash
    var passwordSalt by UserTable.passwordSalt
    var avatar by UserTable.avatar
    var birthdate by UserTable.birthdate
    var createdAt by UserTable.createdAt

    companion object : LongEntityClass<UserEntity>(UserTable)

}