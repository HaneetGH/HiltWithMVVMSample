package com.technorapper.hiltsample.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import com.technorapper.hiltsample.R
import com.technorapper.hiltsample.base.BaseClass
import com.technorapper.hiltsample.data.UserPreferences
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

            viewModel.getName().asLiveData().observe(this, {
                if (it != null) {
                    Log.d("NAME", it)
                }
            })
        viewModel.getAGE().asLiveData().observe(this, {
            if (it != null) {
                Log.d("AGE", it)
            }
        })

        //viewModel.name.observe(this, { Log.d("NAME", it) })
    }
}