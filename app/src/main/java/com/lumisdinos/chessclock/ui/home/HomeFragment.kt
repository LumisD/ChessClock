package com.lumisdinos.chessclock.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.lumisdinos.chessclock.databinding.FragmentHomeBinding
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preference: SharedPreferences

    private lateinit var viewDataBinding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Timber.d("qwer onCreateView")
        viewDataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewDataBinding.viewModel = viewModel
        return viewDataBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
//        viewModel.text.observe(viewLifecycleOwner, Observer {
//            //textView.text = it
//        })

        viewModel.getGame()
    }


    fun setTimeControl(timeControl: String) {
        //viewDataBinding.timeTv.text = time
        viewModel.setTimeControl(timeControl)
    }

}
