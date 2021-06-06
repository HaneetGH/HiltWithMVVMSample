package com.technorapper.hiltsample.ui

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.technorapper.hiltsample.LaunchDetailsQuery
import com.technorapper.hiltsample.PostDetailsMutation
import com.technorapper.hiltsample.base.BaseViewModel
import com.technorapper.hiltsample.data.repository.MainActivityRepository
import com.technorapper.hiltsample.domain.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MainActivityRepository
) : BaseViewModel() {
    val name: MutableLiveData<String> = MutableLiveData()
    private val _dataQueryState: MutableLiveData<DataState<List<LaunchDetailsQuery.Post>?>> =
        MutableLiveData()
    private val _dataMutationStateResponse: MutableLiveData<DataState<PostDetailsMutation.Insert_posts_one>> =
        MutableLiveData()


    val dataQueryState: MutableLiveData<DataState<List<LaunchDetailsQuery.Post>?>>
        get() = _dataQueryState

    val dataMutationStateResponse: MutableLiveData<DataState<PostDetailsMutation.Insert_posts_one>>
        get() = _dataMutationStateResponse

    fun saveData(s: String, s1: String) {

        repository.saveDataInDataStore(s, s1)


    }


    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.ExecutePostQuery -> {
                    repository.queryData(offset = mainStateEvent.offset)
                        .onEach { dataState ->
                            _dataQueryState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.ExecutePostMutation -> {
                    repository.postData(
                        mainStateEvent.description,
                        mainStateEvent.title,
                        mainStateEvent.type,
                        mainStateEvent.videoUri
                    )
                        .onEach { dataState ->
                            dataMutationStateResponse.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class MainStateEvent {

    object GetNameEvent : MainStateEvent()

    object GetAgeEvent : MainStateEvent()
    data class ExecutePostQuery(var offset: Int) : MainStateEvent()
    data class ExecutePostMutation(
        var description: String,
        var title: String,
        var type: String,
        var videoUri: String
    ) : MainStateEvent()

    object None : MainStateEvent()
}