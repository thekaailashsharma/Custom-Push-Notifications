package custom.push.notifications


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import custom.push.notifications.data.UserDataStore
import custom.push.notifications.data.updateTokenToFirebase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.URL


class FirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("FCMToken is $token")
        if (token != null && token.isNotEmpty()) {
            val dataStore = UserDataStore(applicationContext)
            runBlocking {
                dataStore.saveToken(token)
                val isLoggedIn = dataStore.getLogIn.first()
                val email = dataStore.getEmail.first()
                runBlocking {
                    updateTokenToFirebase(
                        context = applicationContext,
                        email = if (isLoggedIn) email else token,
                        idToken = token,
                    )
                }
            }
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            println("Message Image is ${message.data["url"]}")
            try {
                val url = URL(message.data["url"])
                val images: Bitmap? =
                    BitmapFactory.decodeStream(url.openConnection().getInputStream())
                generate(
                    title = message.notification?.title,
                    message = message.notification?.body,
                    image = images
                )
            } catch (e: IOException) {
                println()
            }
        }
    }

    private fun generate(title: String?, message: String?, image: Bitmap?) {

        val channelId = "Notify"
        val channelName = "Push Notifications"
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(notificationChannel)
        try {
            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, channelId)
            notificationBuilder.setContentTitle(title)
                .setSmallIcon(R.drawable.appicon)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(image).setBigContentTitle(title)
                )
                .setAutoCancel(true).setVibrate(longArrayOf(1000, 1000, 1000, 1000))

            manager.notify(50, notificationBuilder.build())
        } catch (e: IOException) {
            println(e)
        }

    }


}