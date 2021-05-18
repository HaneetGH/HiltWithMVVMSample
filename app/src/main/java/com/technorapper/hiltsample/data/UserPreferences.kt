package com.technorapper.hiltsample.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val appContext = context.applicationContext

    val name: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[NAME]
        }

    val age: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[AGE]
        }

     suspend fun saveAGENAME(name: String, age: String) {
        appContext.dataStore.edit { preferences ->
            preferences[NAME] = name
            preferences[AGE] = age
        }
    }

    suspend fun clear() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val NAME = stringPreferencesKey("NAME")
        private val AGE = stringPreferencesKey("AGE")
    }

}