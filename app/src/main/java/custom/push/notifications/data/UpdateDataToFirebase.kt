package custom.push.notifications.data

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import custom.push.notifications.dto.UserTokens


fun updateTokenToFirebase(
    context: Context,
    email: String,
    idToken: String,
    onSuccessClick: () -> Unit = {},
    onFailureClick: () -> Unit = {},
) {
    val profile = UserTokens(
        null,
        idToken
    )
    val db = FirebaseFirestore.getInstance()
    email.let {
        db.collection("UserTokens").document(email).set(profile)
            .addOnSuccessListener {
                Toast.makeText(context, "Token Updated Successfully", Toast.LENGTH_SHORT).show()
                onSuccessClick()

            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Fail to update Profile : " + exception.message,
                    Toast.LENGTH_SHORT
                ).show()
                onFailureClick()
            }
    }

}

fun updateEmailToFirebase(
    context: Context,
    email: String,
    idToken: String,
    profileUrl: String? = null,
    joiningTime: Long? = null,
) {
    val profile = UserTokens(
        email,
        idToken,
        profileUrl,
        joiningTime
    )
    val db = FirebaseFirestore.getInstance()
    email.let {
        db.collection("UserTokens").document(idToken).set(profile)
            .addOnSuccessListener {

                Toast.makeText(context, "Token Updated Successfully", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Fail to update Profile : " + exception.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}



