package custom.push.notifications.dto


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("dl")
    val dl: String?,
    @SerializedName("url")
    val url: String?
)