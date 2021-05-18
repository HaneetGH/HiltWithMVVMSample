package com.technorapper.hiltsample.data.repository


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.technorapper.hiltsample.data.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivityRepository @Inject constructor(
    private val userPreferences: UserPreferences, @ApplicationContext context: Context
) : BaseRepository() {
    private val appContext = context.applicationContext

    fun saveDataInDataStore(s: String, s1: String) {
        runBlocking {
            userPreferences.saveAGENAME(s, s1)
        }
    }

     fun getName(): Flow<String?> {

        return userPreferences.name
    }
    fun getAge(): Flow<String?> {

        return userPreferences.age
    }
}