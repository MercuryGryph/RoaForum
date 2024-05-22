package cn.mercury9.roa.forum.dao

import cn.mercury9.roa.forum.database.DatabaseSingleton.dbQuery
import cn.mercury9.roa.forum.database.UserDataTable
import data.UserData
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.not
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface UserDataDAO {
    suspend fun userNumber(): Int
    suspend fun listUsers(): Map<Int, String>
    suspend fun listUsers(start: Int, end: Int): Map<Int, String>
    suspend fun userInfo(username: String): UserData?
    suspend fun userInfo(id: Int): UserData?
    suspend fun addNewUser(userData: UserData, password: ByteArray): Boolean
    suspend fun updateUserInfo(id: Int, userData: UserData): Boolean
    suspend fun deleteUser(id: Int, username: String): Boolean
}

class DefaultUserDataDAO: UserDataDAO {
    private fun resultRowToUserData(row: ResultRow) = UserData(
        id = row[UserDataTable.id],

        userName = row[UserDataTable.username],
        nickname = row[UserDataTable.nickname],
        minecraftId = row[UserDataTable.minecraftId],

        minecraftUuid = row[UserDataTable.minecraftUuid],
        birthday = row[UserDataTable.birthday],
        gender = row[UserDataTable.gender],

        contacts = row[UserDataTable.contacts]
    )

    override suspend fun userNumber(): Int = dbQuery {
        UserDataTable.selectAll().fetchSize ?: -1
    }

    override suspend fun listUsers(): Map<Int, String> = dbQuery {
        val res = mutableMapOf<Int, String>()
        UserDataTable.selectAll()
            .map {
                it[UserDataTable.id] to it[UserDataTable.username]
            }.forEach {
                res += it
            }
        return@dbQuery res
    }

    override suspend fun listUsers(start: Int, end: Int): Map<Int, String> = dbQuery {
        val res = mutableMapOf<Int, String>()
        UserDataTable.selectAll()
            .where {
                (UserDataTable.id greater start-1) and (UserDataTable.id less end+1)
            }
            .map {
                it[UserDataTable.id] to it[UserDataTable.username]
            }.forEach {
                res += it
            }
        return@dbQuery res
    }

    override suspend fun userInfo(username: String): UserData? = dbQuery {
        UserDataTable.selectAll()
            .where {
                (UserDataTable.username eq username) and (not (UserDataTable.deleted))
            }.map(::resultRowToUserData).singleOrNull()
    }

    override suspend fun userInfo(id: Int): UserData? = dbQuery {
        UserDataTable.selectAll()
            .where {
                (UserDataTable.id eq id) and (not (UserDataTable.deleted))
            }.map(::resultRowToUserData).singleOrNull()
    }

    override suspend fun addNewUser(userData: UserData, password: ByteArray): Boolean = dbQuery {
        UserDataTable.insert {
            it[UserDataTable.id] = userData.id
            it[UserDataTable.username] = userData.userName!!
            it[UserDataTable.nickname] = userData.nickname!!
            it[UserDataTable.minecraftId] = userData.minecraftId!!
            it[UserDataTable.password] = password
        }.resultedValues == null
    }

    override suspend fun updateUserInfo(id: Int, userData: UserData): Boolean = dbQuery {
        UserDataTable.update({
            (UserDataTable.id eq id) and (not (UserDataTable.deleted))
        }) {
            it[UserDataTable.id] = userData.id
            it[UserDataTable.username] = userData.userName!!
            it[UserDataTable.nickname] = userData.nickname!!
            it[UserDataTable.minecraftId] = userData.minecraftId!!
            it[UserDataTable.password] = password
        } > 0
    }

    override suspend fun deleteUser(id: Int, username: String): Boolean = dbQuery {
        UserDataTable.update({
            (UserDataTable.id eq id) and (UserDataTable.username eq username) and (not (UserDataTable.deleted))
        }) {
            it[UserDataTable.deleted] = true
        } > 0
    }
}
