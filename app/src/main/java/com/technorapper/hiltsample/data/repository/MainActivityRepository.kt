package com.technorapper.hiltsample.data.repository

import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.technorapper.hiltsample.LaunchDetailsQuery
import com.technorapper.hiltsample.PostDetailsMutation


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


    suspend fun queryData(offset: Int): Flow<DataState<List<LaunchDetailsQuery.Post>?>> = flow {
        emit(DataState.Loading)
        val response = try {

            apolloClient.query(LaunchDetailsQuery(5, offset)).toDeferred().await()
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
    }

    suspend fun postData(
        description: String,
        title: String,
        type: String,
        videoUri: String
    ): Flow<DataState<PostDetailsMutation.Insert_posts_one>> = flow {

        val response = try {

            apolloClient.mutate(
                PostDetailsMutation(
                    Input.fromNullable(videoUri),
                    Input.fromNullable(type),
                    Input.fromNullable(title),
                    Input.fromNullable(description)
                )
            ).toDeferred().await()
        } catch (e: ApolloException) {
            Log.e("fails", e.toString())
            // handle protocol errors
            return@flow
        }

        val launch = response.data?.insert_posts_one
        if (launch == null || response.hasErrors()) {
            // handle application errors
            return@flow
        }
        Log.e("posgt", launch.title)
        emit(DataState.Success(launch))


    }


}

