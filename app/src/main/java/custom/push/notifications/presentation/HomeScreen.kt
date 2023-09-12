package custom.push.notifications.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jet.firestore.JetFirestore
import com.jet.firestore.getListOfObjects
import custom.push.notifications.MainViewModel
import custom.push.notifications.dto.UserTokens

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    var usersLists by remember { mutableStateOf<List<UserTokens>?>(null) }
    var isDialogVisible = remember { mutableStateOf(false) }
    var currentToken = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        JetFirestore(path = {
            collection("UserTokens")
        }, onRealtimeCollectionFetch = { values, _ ->
            usersLists = values?.getListOfObjects()
        }) {
            if (usersLists?.isNotEmpty() == true) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        vertical = 30.dp,
                        horizontal = 20.dp
                    ),
                    modifier = Modifier.then(
                        if (isDialogVisible.value)
                            Modifier.blur(0.dp) else Modifier
                    )
                ) {
                    items(usersLists ?: emptyList()) {
                        HomeScreenCard(
                            imageUrl = it.profilePictureUrl,
                            email = it.email,
                            timeInMillis = it.joinedTime ?: System.currentTimeMillis(),
                            isDialogVisible = isDialogVisible,
                            token = it.idToken ?: "",
                            currentToken = currentToken
                        )

                    }
                }
                DialogBox(
                    isVisible = isDialogVisible.value,
                    mainViewModel = mainViewModel,
                    successRequest = {
                        mainViewModel.getApiData(
                            title = mainViewModel.notificationTitle.value.text,
                            message = mainViewModel.notificationDescription.value.text,
                            token = currentToken.value
                        )
                    }
                ) {
                    isDialogVisible.value = false
                }
            }

        }
    }
}

@Composable
fun HomeScreenCard(
    imageUrl: String?,
    email: String?,
    timeInMillis: Long,
    token: String,
    isDialogVisible: MutableState<Boolean>,
    currentToken: MutableState<String>,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = getTimeAgo(timeInMillis),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    softWrap = true
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage(
                    imageUrl = imageUrl,
                    initial = 'A',
                    modifier = Modifier
                        .size(75.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.inversePrimary,
                            shape = CircleShape
                        )
                        .padding(3.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = email ?: "",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.surfaceTint,
                    softWrap = true,
                    modifier = Modifier.padding(end = 7.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(onClick = {
                    isDialogVisible.value = true
                    currentToken.value = token
                }) {
                    Text(
                        text = "👋  Wave to say Hi",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.surfaceTint,
                        softWrap = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

fun getTimeAgo(timeInMillis: Long): String {
    val now = System.currentTimeMillis()
    val diffMillis = now - timeInMillis

    val seconds = diffMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        seconds < 60 -> "$seconds seconds ago"
        minutes < 60 -> "$minutes minutes ago"
        hours < 24 -> "$hours hours ago"
        days < 7 -> "$days days ago"
        weeks < 4 -> "$weeks weeks ago"
        months < 12 -> "$months months ago"
        else -> "$years years ago"
    }
}

@Composable
fun DialogBox(
    isVisible: Boolean,
    mainViewModel: MainViewModel,
    icon: ImageVector = Icons.Filled.Notifications,
    title: String = "Send a notification to the user",
    successRequest: () -> Unit = {},
    dismissRequest: () -> Unit,
) {
    if (isVisible) {


        Dialog(onDismissRequest = dismissRequest) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    Modifier
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 35.dp)
                            .height(70.dp)
                            .fillMaxWidth(),
                        tint = MaterialTheme.colorScheme.surfaceTint

                        )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(top = 0.dp)
                                .fillMaxWidth(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.surfaceTint
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextFieldWithIcons(
                            textValue = "Title",
                            placeholder = "🤭 Send Privately ",
                            icon = Icons.Filled.Face,
                            mutableText = mainViewModel.notificationTitle.value,
                            onValueChanged = {
                                mainViewModel.notificationTitle.value = it
                            },
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextFieldWithIcons(
                            textValue = "Description",
                            placeholder = "\uD83D\uDC4B  Wave to say Hi",
                            icon = Icons.Filled.FavoriteBorder,
                            mutableText = mainViewModel.notificationDescription.value,
                            onValueChanged = {
                                mainViewModel.notificationDescription.value = it
                            },
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next,
                        )


                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(onClick = successRequest) {
                            Text(
                                "🔔  Send Notification ",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surfaceTint,
                                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithIcons(
    textValue: String,
    placeholder: String,
    icon: ImageVector,
    mutableText: TextFieldValue,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onValueChanged: (TextFieldValue) -> Unit,
) {
    TextField(
        value = mutableText,
        leadingIcon = {
            Icon(
                imageVector = icon,
                tint = MaterialTheme.colorScheme.inversePrimary,
                contentDescription = "Icon"
            )
        },
        onValueChange = onValueChanged,
        label = {
            Text(
                text = textValue,
                color = MaterialTheme.colorScheme.surfaceTint
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.surfaceTint
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        modifier = Modifier
            .padding(start = 15.dp, top = 5.dp, bottom = 5.dp, end = 15.dp)
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors())
}
