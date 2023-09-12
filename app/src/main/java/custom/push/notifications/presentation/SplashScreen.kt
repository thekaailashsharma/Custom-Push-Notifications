package custom.push.notifications.presentation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import custom.push.notifications.R
import custom.push.notifications.data.UserDataStore
import custom.push.notifications.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val isLoggedIn by dataStore.getLogIn.collectAsState(false)
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.35f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        delay(2000L)
        navController.popBackStack()
        if (isLoggedIn) navController.navigate(Screens.HomeScreen.route)
        else navController.navigate(Screens.LoginScreen.route)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.appicon),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}