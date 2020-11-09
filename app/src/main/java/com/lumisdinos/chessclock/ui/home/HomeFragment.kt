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
import com.lumisdinos.chessclock.common.utils.isClickedSingle
import com.lumisdinos.chessclock.data.model.GameState
import com.lumisdinos.chessclock.databinding.FragmentHomeBinding
import com.lumisdinos.chessclock.dialogs.DialogListener
import com.lumisdinos.chessclock.dialogs.alertDialogToSetCustomTime
import com.lumisdinos.chessclock.dialogs.getAlertDialog
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment(), DialogListener {

    private val actionSetCustomTime = "101"
    private val actionTimeExpired = "102"

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
        viewDataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewDataBinding.viewModel = viewModel
        return viewDataBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        (activity as MainActivity).navigationItemSelected.observe(viewLifecycleOwner,
            { setChosenTimeControl(it) })
        viewModel.gameState.observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.openDrawer.observe(viewLifecycleOwner, Observer { openDrawer(it) })
    }


    override fun onResume() {
        super.onResume()
        viewModel.getGame()
    }


    override fun onPause() {
        super.onPause()
        viewModel.saveGame()
    }


    private fun setChosenTimeControl(event: Event<String>) {
        if (isClickedSingle()) return
        event.getContentIfNotHandled()?.let {
            if (it == getString(R.string.custom_time)) {
                showDialogToSetCustomTime()
            } else {
                viewModel.setChosenTimeControl(it)
            }
        }
    }


    private fun openDrawer(event: Event<Boolean>) {
        if (isClickedSingle()) return
        event.getContentIfNotHandled()?.let {
            val mainActivity = activity as MainActivity
            mainActivity.openDrawer()
        }
    }


    private fun render(gameState: GameState) {
        moveSound(gameState.moveSound)
        timeExpired(gameState.timeExpired)
    }


    private fun moveSound(event: Event<Boolean>) {
        event.getContentIfNotHandled()?.let {
            if (it) {
                val mp = MediaPlayer.create(context, R.raw.pawn_move)
                mp.start()

                mp.setOnCompletionListener(OnCompletionListener {
                    mp.release()
                })
            }
        }
    }


    private fun timeExpired(event: Event<String>) {
        event.getContentIfNotHandled()?.let {
            if (it.isEmpty()) return
            getAlertDialog(
                requireContext(),
                actionTimeExpired,
                this,
                getString(R.string.time_is_over),//title
                String.format(getString(R.string._lost_on_time), it),//message
                getString(R.string.ok)
            ).show()

        }
    }


    private fun showDialogToSetCustomTime() {
        alertDialogToSetCustomTime(
            requireContext(),
            LayoutInflater.from(requireContext()),
            actionSetCustomTime,//action
            this,
            getString(R.string.custom_time),//title
            getString(R.string.set_min_sec_inc),//message
            getString(R.string.ok),
            getString(R.string.cancel)
        ).show()
    }


    //  -- DialogListener --

    override fun onPositiveDialogClick(result: List<String>) {
        when (result[0]) {
            actionSetCustomTime -> {
                viewModel.setChosenTimeControl(result[1])
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
