package com.technorapper.hiltsample.ui

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
    private val _dataState: MutableLiveData<DataState<Flow<String?>>> = MutableLiveData()
    val dataState: MutableLiveData<DataState<Flow<String?>>>
        get() = _dataState
    fun saveData(s: String, s1: String) {

        repository.saveDataInDataStore(s, s1)


    }

//     fun getName(): Flow<String?> {
//
//
//        return repository.getName()
//
//    }
//
//    fun getAGE() {
//
//
//        return repository.getAge()
//
//    }

    fun setStateEvent(mainStateEvent: MainStateEvent){
        viewModelScope.launch {
            when(mainStateEvent){
                is MainStateEvent.GetNameEvent -> {
                    repository.getIntentName()
                        .onEach {dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class MainStateEvent{

    object GetNameEvent: MainStateEvent()

    object GetAgeEvent: MainStateEvent()

    object None: MainStateEvent()
}