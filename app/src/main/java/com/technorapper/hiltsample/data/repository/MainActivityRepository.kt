package com.technorapper.hiltsample.data.repository


import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.technorapper.hiltsample.LaunchDetailsQuery

import com.technorapper.hiltsample.data.UserPreferences
import com.technorapper.hiltsample.domain.DataState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainActivityRepository @Inject constructor(
    private val userPreferences: UserPreferences,
    @ApplicationContext context: Context,
    private val apolloClient: ApolloClient
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

    suspend fun queryData(offset:Int): Flow<DataState<List<LaunchDetailsQuery.Post>?>> = flow {
        // in your coroutine scope, call `ApolloClient.query(...).toDeferred().await()`
        //   emit(DataState.Loading)

        emit(DataState.Loading)
        val response = try {

            apolloClient.query(LaunchDetailsQuery(5,offset)).toDeferred().await()
        } catch (e: ApolloException) {
            Log.e("fails", e.toString())
            // handle protocol errors
            return@flow
        }

        val launch = response.data?.posts
        if (launch == null || response.hasErrors()) {
            // handle application errors
            return@flow
        }
        emit(DataState.Success(launch))
        // launch now contains a typesafe model of your data
        // println("Launch site: ${launch[0].title}")

    }


    suspend fun getIntentName(): Flow<DataState<Flow<String?>>> = flow {
        emit(DataState.Loading)
        delay(1000)
        val networkBlogs = userPreferences.name
        //apiHit()
        emit(DataState.Success(networkBlogs))
    }

    suspend fun getIntentAGE(): Flow<DataState<Flow<String?>>> = flow {
        emit(DataState.Loading)
        delay(1000)
        val networkBlogs = userPreferences.age
        emit(DataState.Success(networkBlogs))
    }

    fun getAge(): Flow<String?> {

        return userPreferences.age
    }


}

