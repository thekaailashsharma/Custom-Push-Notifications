package custom.push.notifications

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import custom.push.notifications.apiImpl.ApiService
import custom.push.notifications.dto.Data
import custom.push.notifications.dto.Notification
import custom.push.notifications.dto.NotifyPrompt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject
constructor(
    application: Application,
    private val repository: ApiService,
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    var notificationTitle = mutableStateOf(TextFieldValue(""))

    var notificationDescription = mutableStateOf(TextFieldValue(""))


    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun getApiData(
        title: String,
        message: String,
        token: String,
    ) {
        viewModelScope.launch {
            val result = repository.getApiData(
                NotifyPrompt(
                    data = Data(
                        dl = null,
                        url = "https://static-00.iconduck.com/assets.00/bell-emoji-2048x2048-0742g350.png"
                    ),
                    notification = Notification(
                        title = title,
                        body = message,
                        sound = "Tri-tone",
                        mutableContent = true
                    ),
                    to = token
                )
            )

        }
    }

}