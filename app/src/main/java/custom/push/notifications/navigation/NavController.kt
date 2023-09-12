package custom.push.notifications.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import custom.push.notifications.MainViewModel
import custom.push.notifications.presentation.HomeScreen
import custom.push.notifications.presentation.LoginScreen
import custom.push.notifications.presentation.SplashScreen

@Composable
fun MainNavController(mainViewModel: MainViewModel) {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = Screens.SplashScreen.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300),
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300),
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300),
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300),
            )
        },

        ) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(mainViewModel, navHostController)
        }
        composable(Screens.SplashScreen.route) {
            SplashScreen(navController = navHostController)
        }
        composable(Screens.HomeScreen.route) {
            HomeScreen(mainViewModel = mainViewModel)
        }


    }
}