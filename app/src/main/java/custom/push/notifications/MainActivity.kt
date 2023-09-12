package custom.push.notifications

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.google.firebase.messaging.FirebaseMessaging
import custom.push.notifications.data.UserDataStore
import custom.push.notifications.navigation.MainNavController
import custom.push.notifications.ui.theme.CustomPushNotificationsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {

            }
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        setContent {
            CustomPushNotificationsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val userDataStore = UserDataStore(this)
                    val isLoggedIn = userDataStore.getLogIn.collectAsState(initial = false)
                    val mainViewModel by viewModels<MainViewModel>()
                    MainNavController(mainViewModel = mainViewModel)

                }
            }
        }
    }
}



