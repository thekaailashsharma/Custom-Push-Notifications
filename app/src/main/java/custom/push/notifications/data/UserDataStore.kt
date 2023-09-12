package custom.push.notifications.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore("pref")

class UserDataStore(private val context: Context) {
    companion object {
        val isLoggedIn = booleanPreferencesKey("isLoggedIn")
        val email = stringPreferencesKey("email")
        val token = stringPreferencesKey("deviceToken")
        val pfp = stringPreferencesKey("pfp")
    }

    val getLogIn: Flow<Boolean> = context.datastore.data.map {
        it[isLoggedIn] ?: false
    }


    val getEmail: Flow<String> = context.datastore.data.map {
        it[email] ?: ""
    }

    val deviceToken: Flow<String> = context.datastore.data.map {
        it[token] ?: ""
    }

    val userPfp: Flow<String> = context.datastore.data.map {
        it[pfp] ?: ""
    }

    suspend fun saveLogIn(loggedIn: Boolean) {
        context.datastore.edit {
            it[isLoggedIn] = loggedIn
        }
    }

    suspend fun saveEmail(userEmail: String) {
        context.datastore.edit {
            it[email] = userEmail
        }
    }

    suspend fun saveToken(userToken: String) {
        context.datastore.edit {
            it[token] = userToken
        }
    }

    suspend fun savePfp(userPfp: String) {
        context.datastore.edit {
            it[pfp] = userPfp
        }
    }
}