package com.fiap.comunidadeconectada.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "comunidade_conectada_prefs"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class PreferencesManager(private val context: Context) {

    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val THEME_DARK_MODE = booleanPreferencesKey("theme_dark_mode")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val LOCATION_ENABLED = booleanPreferencesKey("location_enabled")
    }

    // Salvar ID do usuário
    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    // Obter ID do usuário
    fun getUserId(): Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_ID] }

    // Salvar nome do usuário
    suspend fun saveUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    // Obter nome do usuário
    fun getUserName(): Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_NAME] }

    // Salvar email do usuário
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    // Obter email do usuário
    fun getUserEmail(): Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL] }

    // Salvar token de autenticação
    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    // Obter token de autenticação
    fun getAuthToken(): Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[AUTH_TOKEN] }

    // Salvar status de login
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    // Verificar se está logado
    fun isLoggedIn(): Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN] ?: false }

    // Salvar preferência de tema
    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_DARK_MODE] = isDarkMode
        }
    }

    // Obter preferência de tema
    fun isDarkMode(): Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[THEME_DARK_MODE] ?: false }

    // Salvar preferência de notificações
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    // Obter preferência de notificações
    fun isNotificationsEnabled(): Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[NOTIFICATIONS_ENABLED] ?: true }

    // Salvar preferência de localização
    suspend fun setLocationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOCATION_ENABLED] = enabled
        }
    }

    // Obter preferência de localização
    fun isLocationEnabled(): Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[LOCATION_ENABLED] ?: false }

    // Limpar todas as preferências (logout)
    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
