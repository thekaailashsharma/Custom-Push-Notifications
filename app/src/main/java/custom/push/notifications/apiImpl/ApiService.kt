package custom.push.notifications.apiImpl

import custom.push.notifications.dto.NotifyPeople
import custom.push.notifications.dto.NotifyPrompt


interface ApiService {
    suspend fun getApiData(notifyPrompt: NotifyPrompt): NotifyPeople?
}