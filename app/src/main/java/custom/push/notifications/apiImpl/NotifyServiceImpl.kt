package custom.push.notifications.apiImpl


import android.util.Log
import custom.push.notifications.BuildConfig
import custom.push.notifications.dto.NotifyPeople
import custom.push.notifications.dto.NotifyPrompt
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.url

class ApiServiceImpl(
    private val client: HttpClient,
) : ApiService {

    override suspend fun getApiData(notifyPrompt: NotifyPrompt): NotifyPeople? {
        try {
            val a = client.post<NotifyPeople> {
                url("https://fcm.googleapis.com/fcm/send")
                body = notifyPrompt
                headers {
                    this.append("Content-Type", "application/json")
                    this.append(
                        "Authorization", "key=" +
                                BuildConfig.API_KEY
                    )
                }
            }
            return a
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return null
        }
    }
}
