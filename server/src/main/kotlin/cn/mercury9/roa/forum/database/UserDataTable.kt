package cn.mercury9.roa.forum.database

import data.Contact
import org.jetbrains.exposed.sql.Table

object UserDataTable: Table() {
    val id = this.integer("id").autoIncrement()
    val deleted = this.bool("deleted")

    val username = this.varchar("username", 20)
    val password = this.binary("password", 32)
    val nickname = this.varchar("nickname",20)
    val minecraftId = this.varchar("minecraftId", 25)

    val minecraftUuid = this.varchar("minecraftUuid", 32).nullable()
    val birthday = this.varchar("birthday", 12).nullable()
    val gender = this.varchar("gender", 16).nullable()

    val contacts = this.array<Contact>("contacts").nullable()

    override val primaryKey = this.PrimaryKey(id)
}
