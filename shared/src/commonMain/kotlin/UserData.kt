import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val userName: String? = null,
    val nickName: String? = null
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

fun isNickNameLegal(nickName: String): StringLegalState {

    return StringLegalState.Unchecked
}
