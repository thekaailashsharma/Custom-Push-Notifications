package custom.push.notifications.dto


import com.google.gson.annotations.SerializedName

data class NotifyPeople(
    @SerializedName("canonical_ids")
    val canonicalIds: Int?,
    @SerializedName("failure")
    val failure: Int?,
    @SerializedName("multicast_id")
    val multicastId: Long?,
    @SerializedName("results")
    val results: List<Result?>?,
    @SerializedName("success")
    val success: Int?
)