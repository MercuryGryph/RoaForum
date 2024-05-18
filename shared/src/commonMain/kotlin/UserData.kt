import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userName: String? = null,
    val nickName: String? = null
)

fun isUserNameLegal(userName: String): StringLegalState {
    if (userName.isEmpty()) return StringLegalState.Empty
    if (userName.length !in 4..20) return StringLegalState.WrongLength

    val pattern = Regex("/^[a-zA-Z]\\w+$/")
    if (pattern.matches(userName)) {
        return StringLegalState.Legal
    }
    return StringLegalState.HasIllegalChar
}

fun isPasswordLegal(password: String): StringLegalState {
    if (password.isEmpty()) return StringLegalState.Empty
    if (password.length !in 6..20) return StringLegalState.WrongLength

    val pattern = Regex("/^\\w+$/]")
    if (pattern.matches(password)) {
        return StringLegalState.Legal
    }
    return StringLegalState.Unchecked
}

fun isNickNameLegal(nickName: String): StringLegalState {

    return StringLegalState.Unchecked
}
