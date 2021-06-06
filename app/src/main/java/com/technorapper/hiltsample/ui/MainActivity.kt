package com.technorapper.hiltsample.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import com.technorapper.hiltsample.LaunchDetailsQuery
import com.technorapper.hiltsample.PostDetailsMutation
import com.technorapper.hiltsample.R
import com.technorapper.hiltsample.base.BaseClass
import com.technorapper.hiltsample.data.model.RequestPost
import com.technorapper.hiltsample.databinding.ActivityMainBinding
import com.technorapper.hiltsample.domain.DataState
import com.technorapper.hiltsample.ui.adapter.DataListAdapter
import com.technorapper.hiltsample.utils.BottomUpDialogs
import com.technorapper.hiltsample.utils.RecyclerViewClickListener
import kotlinx.coroutines.flow.Flow

/*
* Once Hilt is set up in your Application class and an application-level component is available, Hilt can provide dependencies to other Android classes that have the @AndroidEntryPoint annotation:
* */

class MainActivity : BaseClass(), RecyclerViewClickListener {
    private val viewModel by viewModels<MainActivityViewModel>()
    var offset = 1
    lateinit var adapter: DataListAdapter
    lateinit var binding: ActivityMainBinding
    var list: ArrayList<LaunchDetailsQuery.Post> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun setBinding() {
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        setAdapter();
        setClicks()
    }

    private fun setClicks() {
        binding.fab.setOnClickListener {

            BottomUpDialogs.showAddDialog(this) {
                if (it != null) {
                    var request =
                        it as RequestPost
                    viewModel.setStateEvent(
                        MainStateEvent.ExecutePostMutation(
                            request.description,
                            request.title,
                            request.type,
                            request.url
                        )
                    )
                }

            };
        }
    }

    private fun setAdapter() {
        adapter = DataListAdapter(list, this)
        binding.adapter = adapter

        adapter.onEndOfListReached = {
            viewModel.setStateEvent(MainStateEvent.ExecutePostQuery(offset++))
        }
    }


    override fun attachViewModel() {
       // viewModel.saveData("Haneet", "28");
        viewModel.dataMutationStateResponse.observe(this, { it ->
            if (it != null) {

                when (it) {
                    is DataState.Success<PostDetailsMutation.Insert_posts_one> -> {
                        offset = 1
                        viewModel.setStateEvent(MainStateEvent.ExecutePostQuery(offset))
                        Log.d("DATA  RES", "SUCCESSz");
                    }
                    is DataState.Error -> {
                        Log.d("DATA  RES", "ERROR");
                    }
                    is DataState.Loading -> {
                        Log.d("DATA  RES", "LOADING");
                    }
                }
            }

        })
        // viewModel.setStateEvent(MainStateEvent.GetNameEvent)
        //viewModel.setStateEvent(MainStateEvent.GetAgeEvent)
        viewModel.setStateEvent(MainStateEvent.ExecutePostQuery(offset))
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
                        if (it?.data != null) {
                            if (offset == 1)
                                list.clear()
                            list.addAll(it.data)
                            adapter.notifyDataSetChanged()
                        }

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

    override fun onClick(v: View?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(v: View?, position: Int) {
        TODO("Not yet implemented")
    }
}