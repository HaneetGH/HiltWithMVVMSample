package com.technorapper.hiltsample.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.technorapper.hiltsample.LaunchDetailsQuery
import com.technorapper.hiltsample.R
import com.technorapper.hiltsample.base.BaseClass
import com.technorapper.hiltsample.data.UserPreferences
import com.technorapper.hiltsample.domain.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/*
* Once Hilt is set up in your Application class and an application-level component is available, Hilt can provide dependencies to other Android classes that have the @AndroidEntryPoint annotation:
* */

class MainActivity : BaseClass() {
    private val viewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel.saveData("Haneet", "28");

        viewModel.setStateEvent(MainStateEvent.GetNameEvent)
        viewModel.setStateEvent(MainStateEvent.GetAgeEvent)
        viewModel.setStateEvent(MainStateEvent.ExecutePostQuery)
        viewModel.dataState.observe(this, { it ->
            if (it != null) {

                when (it) {
                    is DataState.Success<Flow<String?>> -> {
                        it.data.asLiveData().observe(this, { final ->
                            if (final != null) {
                                Log.d("DATA", final)
                            }
                        })
                    }
                    is DataState.Error -> {
                        Log.d("DATA", "ERROR");
                    }
                    is DataState.Loading -> {
                        Log.d("DATA", "LOADING");
                    }
                }
            }

        })
        viewModel.dataQueryState.observe(this, { it ->
            if (it != null) {

                when (it) {
                    is DataState.Success<List<LaunchDetailsQuery.Post>?> -> {
                        Log.d("DATA", it.data?.get(0)!!.description.toString());
                    }
                    is DataState.Error -> {
                        Log.d("DATA", "ERROR");
                    }
                    is DataState.Loading -> {
                        Log.d("DATA", "LOADING");
                    }
                }
            }

        })

        //viewModel.name.observe(this, { Log.d("NAME", it) })
    }
}