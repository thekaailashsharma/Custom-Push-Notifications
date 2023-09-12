package custom.push.notifications.presentation

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.Identity
import custom.push.notifications.GoogleAuthUiClient
import custom.push.notifications.MainViewModel
import custom.push.notifications.R
import custom.push.notifications.data.UserDataStore
import custom.push.notifications.data.updateEmailToFirebase
import custom.push.notifications.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(mainViewModel: MainViewModel, navController: NavController) {
    val userDataStore = UserDataStore(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    val state by mainViewModel.state.collectAsState()
    val currentToken = userDataStore.deviceToken.collectAsState(initial = "")
    LaunchedEffect(key1 = state.isSignInSuccessful) {
        Log.i("TypeChanged", "isSignInSuccessful")
        if (state.isSignInSuccessful) {
            updateEmailToFirebase(
                context,
                email = googleAuthUiClient.getSignedInUser()?.email ?: "",
                idToken = currentToken.value,
                profileUrl = googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                joiningTime = System.currentTimeMillis()
            )
            Log.i("TypeChangedCalled", "isSignInSuccessful")
            Toast.makeText(
                context,
                "Sign in successful as ${googleAuthUiClient.getSignedInUser()?.email}",
                Toast.LENGTH_LONG
            ).show()
            userDataStore.saveLogIn(true)
            userDataStore.saveEmail(googleAuthUiClient.getSignedInUser()?.email ?: "")
            userDataStore.savePfp(googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "")
            mainViewModel.resetState()
            navController.popBackStack()
            navController.navigate(Screens.HomeScreen.route)

        }
    }

    LaunchedEffect(key1 = googleAuthUiClient) {
        Log.i("Auth-Client", googleAuthUiClient.getSignedInUser().toString())
        if (googleAuthUiClient.getSignedInUser()?.username != null) {
            updateEmailToFirebase(
                context,
                email = googleAuthUiClient.getSignedInUser()?.email ?: "",
                idToken = currentToken.value,
                profileUrl = googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                joiningTime = System.currentTimeMillis()
            )
            Log.i("Auth-Client2.0", googleAuthUiClient.getSignedInUser()?.email ?: "")
            Log.i("Auth-Client2.0", googleAuthUiClient.getSignedInUser()?.username ?: "")
            Log.i("Auth-Client2.0", googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "")
            userDataStore.saveLogIn(true)
            userDataStore.saveEmail(googleAuthUiClient.getSignedInUser()?.email ?: "")
            navController.popBackStack()
            navController.navigate(Screens.HomeScreen.route)
        }
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    mainViewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SocialLoginButton(
            imageVector = R.drawable.google,
            text = "Login With Google",
        ) {
            coroutineScope.launch {
                val signInIntentSender = googleAuthUiClient.signIn()
                launcher.launch(
                    IntentSenderRequest.Builder(
                        signInIntentSender ?: return@launch
                    ).build()
                )

            }

        }

    }
}

