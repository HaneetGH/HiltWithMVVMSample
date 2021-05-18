package com.technorapper.hiltsample.ui

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.technorapper.hiltsample.base.BaseViewModel
import com.technorapper.hiltsample.data.repository.MainActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: MainActivityRepository
) : BaseViewModel() {
    val name: MutableLiveData<String> = MutableLiveData()

    fun saveData(s: String, s1: String) {

        repository.saveDataInDataStore(s, s1)


    }

     fun getName(): Flow<String?> {


        return repository.getName()

    }

    fun getAGE(): Flow<String?> {


        return repository.getAge()

    }
}