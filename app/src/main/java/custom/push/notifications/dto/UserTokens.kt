package custom.push.notifications.dto

data class UserTokens(
    val email: String? = null,
    val idToken: String? = null,
    val profilePictureUrl: String? = null,
    val joinedTime: Long? = null,
) {
    constructor() : this("", "", "", 0)
}