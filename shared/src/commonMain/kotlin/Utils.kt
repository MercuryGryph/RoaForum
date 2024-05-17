import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun String.sha256() =
    MessageDigest
        .getInstance("SHA-256")
        .digest(toByteArray())
        .toHexString()
