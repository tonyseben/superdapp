package com.example.superdapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    companion object {
        private val SESSION_TOPIC_KEY = stringPreferencesKey("session_topic")
    }

    override suspend fun saveSession(sessionTopic: String) {
        dataStore.edit { preferences ->
            preferences[SESSION_TOPIC_KEY] = sessionTopic
        }
    }

    override suspend fun getSession(): String? {
        val preferences = dataStore.data.first()
        val sessionTopic = preferences[SESSION_TOPIC_KEY] ?: return null
        return sessionTopic
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences[SESSION_TOPIC_KEY] = ""
        }
    }
}
