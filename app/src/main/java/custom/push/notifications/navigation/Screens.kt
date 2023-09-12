package custom.push.notifications.navigation

sealed class Screens(val route: String) {
    object LoginScreen : Screens("login")
    object SplashScreen : Screens("splash")
    object HomeScreen : Screens("home")


}