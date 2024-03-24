package com.birthdayapp.sample.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

val Context.dataStore by preferencesDataStore(name = "profilePrefs")

class ProfilePreferences(private val appContext: Context) {
    private val photoPathKey = stringPreferencesKey("photoPathKey")
    private val babyNameKey = stringPreferencesKey("babyNameKey")
    private val babyBirthKey = longPreferencesKey("babyBirthKey")

    suspend fun getPhotoPath(): String? {
        return appContext.dataStore.data.firstOrNull()?.get(photoPathKey)
    }

    suspend fun getName(): String? {
        return appContext.dataStore.data.firstOrNull()?.get(babyNameKey)
    }

    suspend fun getBirthDate(): Long? {
        return appContext.dataStore.data.firstOrNull()?.get(babyBirthKey)
    }

    suspend fun setPhotoPath(path: String) {
        appContext.dataStore.edit { preferences ->
            preferences[photoPathKey] = path
        }
    }

    suspend fun setName(name: String) {
        appContext.dataStore.edit { preferences ->
            preferences[babyNameKey] = name
        }
    }

    suspend fun setBirthdate(date: Long) {
        appContext.dataStore.edit { preferences ->
            preferences[babyBirthKey] = date
        }
    }
}
