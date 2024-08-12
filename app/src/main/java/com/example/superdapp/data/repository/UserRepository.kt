package com.example.superdapp.data.repository

interface UserRepository {
    suspend fun saveSession(sessionTopic: String)
    suspend fun getSession(): String?
    suspend fun clearSession()
}
