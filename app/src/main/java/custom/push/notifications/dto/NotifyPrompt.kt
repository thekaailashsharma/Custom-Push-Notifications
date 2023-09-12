package custom.push.notifications.dto


import com.google.gson.annotations.SerializedName

data class NotifyPrompt(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("notification")
    val notification: Notification?,
    @SerializedName("to")
    val to: String?
)