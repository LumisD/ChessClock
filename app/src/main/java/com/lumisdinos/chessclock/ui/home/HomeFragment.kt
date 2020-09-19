package com.lumisdinos.chessclock.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lumisdinos.chessclock.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Timber.d("qwer onCreateView")
        viewDataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.doSom()
        viewDataBinding.viewModel = viewModel
        return viewDataBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        viewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
    }


    fun setTime(time: String) {
        viewDataBinding.timeTv.text = time
    }

}
