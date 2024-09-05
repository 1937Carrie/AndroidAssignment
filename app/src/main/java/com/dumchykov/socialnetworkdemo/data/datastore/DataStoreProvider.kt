package com.dumchykov.socialnetworkdemo.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dumchykov.socialnetworkdemo.data.datastore.DataStoreProvider.Companion.PREFERENCES_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreProvider @Inject constructor(private val context: Context) {

    private fun readString(key: String): Flow<String> {
        val preferencesKey = stringPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: ""
        }
    }

    private suspend fun writeString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    fun readCredentials(): Flow<Pair<String, String>> {
        return readString(KEY_EMAIL)
            .combine(readString(KEY_PASSWORD)) { email, password -> email to password }
            .distinctUntilChanged()
    }

    suspend fun writeCredentials(email: String, password: String) {
        writeString(KEY_EMAIL, email)
        writeString(KEY_PASSWORD, password)
    }

    suspend fun clearCredentials() {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(KEY_EMAIL))
            preferences.remove(stringPreferencesKey(KEY_PASSWORD))
        }
    }

    companion object {
        const val PREFERENCES_NAME = "miscellaneous"
        const val KEY_EMAIL = "email"
        const val KEY_PASSWORD = "password"
    }
}