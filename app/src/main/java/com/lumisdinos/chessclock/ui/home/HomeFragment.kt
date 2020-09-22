package com.lumisdinos.chessclock.ui.home

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lumisdinos.chessclock.MainActivity
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.common.Event
import com.lumisdinos.chessclock.common.utils.isClickedShort
import com.lumisdinos.chessclock.common.utils.isClickedSingle
import com.lumisdinos.chessclock.databinding.FragmentHomeBinding
import com.lumisdinos.chessclock.dialogs.DialogListener
import com.lumisdinos.chessclock.dialogs.getAlertDialog
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : DaggerFragment(), DialogListener {

    private val ACTION_TIME_EXPIRED = "110"

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
        viewModel.timeExpired.observe(viewLifecycleOwner, Observer { timeExpired(it) })
        viewModel.openDrawer.observe(viewLifecycleOwner, Observer { openDrawer(it) })
        viewModel.moveSound.observe(viewLifecycleOwner, Observer { moveSound(it) })
    }


    override fun onResume() {
        super.onResume()
        Timber.d("qwer onResume")
        viewModel.getGame()
    }


    override fun onPause() {
        super.onPause()
        Timber.d("qwer onPause")
        viewModel.saveGame()
    }


    fun setChosenTimeControl(timeControl: String) {
        viewModel.setChosenTimeControl(timeControl)
    }


    private fun openDrawer(event: Event<Boolean>) {
        if (isClickedSingle()) return
        event.getContentIfNotHandled()?.let {
            val mainActivity = activity as MainActivity
            mainActivity.openDrawer()
        }
    }


    private fun moveSound(event: Event<Boolean>) {
        if (isClickedShort()) return
        event.getContentIfNotHandled()?.let {
            val mp = MediaPlayer.create(context, R.raw.pawn_move)
            mp.start()

            mp.setOnCompletionListener(OnCompletionListener {
                Timber.d("qwer mp.release")
                mp.release()
            })
        }
    }


    private fun timeExpired(event: Event<String>) {
        if (isClickedSingle()) return
        event.getContentIfNotHandled()?.let {
            Timber.d("qwer timeExpired")
            getAlertDialog(
                requireContext(),
                ACTION_TIME_EXPIRED,
                this,
                getString(R.string.time_is_over),//title
                String.format(getString(R.string._lost_on_time), it),//message
                getString(R.string.ok)
            ).show()

        }
    }


    //  -- DialogListener --

    override fun onPositiveDialogClick(result: List<String>) {
        Timber.d("qwer onPositiveDialogClick action: %s", result[0])
        when (result[0]) {
            ACTION_TIME_EXPIRED -> {
            }
            else -> {
            }
        }
    }

    override fun onNegativeDialogClick(result: List<String>) {
    }

    override fun onNeutralDialogClick(result: List<String>) {
    }

}
