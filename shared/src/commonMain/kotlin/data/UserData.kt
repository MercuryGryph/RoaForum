package data

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userName: String? = null,
    val nickname: String? = null,
    val minecraftId: String? = null,
    val minecraftUuid: String? = null,
    val gender: String? = null,
    val birthday: String? = null,
    val contacts: List<Contact>? = null
)

@Serializable
data class Contact(
    val type: String,
    val content: String,
    val isPublic: Boolean
)

fun isUserNameLegal(userName: String): StringLegalState {
    if (userName.isEmpty()) return StringLegalState.Empty
    if (userName.length !in 4..20) return StringLegalState.WrongLength

    if (Regex("""^[a-zA-Z]\w+$""").matches(userName)) {
        return StringLegalState.Legal
    }
    return StringLegalState.HasIllegalChar
}

fun isPasswordLegal(password: String): StringLegalState {
    if (password.isEmpty()) return StringLegalState.Empty
    if (password.length !in 6..20) return StringLegalState.WrongLength

    if (Regex("""^\w+$""").matches(password)) {
        return StringLegalState.Legal
    }
    return StringLegalState.HasIllegalChar
}

fun isNicknameLegal(nickName: String): StringLegalState {
    if (nickName.isEmpty()) return StringLegalState.Empty
    if (nickName.length < 4) return StringLegalState.TooShort
    if (nickName.length > 32) return StringLegalState.TooLong

    if (nickName.contains(
            """[`~!@#$%^&*()+=<>?:"{}|,./;'\\\[\]·！￥…（）—《》？：“”【】、；‘，。＠＃％＆＊]"""
            .toRegex()) ||
        !"""\S|( )""".toRegex().matches(nickName)
    ) {
        return StringLegalState.HasIllegalChar
    }

    return StringLegalState.Legal
}
