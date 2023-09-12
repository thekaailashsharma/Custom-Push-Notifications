package custom.push.notifications.dto


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("body")
    val body: String?,
    @SerializedName("mutable_content")
    val mutableContent: Boolean?,
    @SerializedName("sound")
    val sound: String?,
    @SerializedName("title")
    val title: String?
)