package com.lumisdinos.chessclock.ui.home

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.lumisdinos.chessclock.ui.MainActivity
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.common.Event
import com.lumisdinos.chessclock.common.utils.convertButtonTextColor
import com.lumisdinos.chessclock.common.utils.convertChangePausedIcon
import com.lumisdinos.chessclock.common.utils.convertRestClockTime
import com.lumisdinos.chessclock.common.utils.convertTimeControlFromGame
import com.lumisdinos.chessclock.common.utils.isClickedSingle
import com.lumisdinos.chessclock.common.utils.setButtonsBG
import com.lumisdinos.chessclock.data.model.GameState
import com.lumisdinos.chessclock.databinding.FragmentHomeBinding
import com.lumisdinos.chessclock.dialogs.DialogListener
import com.lumisdinos.chessclock.dialogs.alertDialogToSetCustomTime
import com.lumisdinos.chessclock.dialogs.getAlertDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), DialogListener {

    private val actionSetCustomTime = "101"
    private val actionTimeExpired = "102"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        subscribeUi(binding)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.gameState.observe(viewLifecycleOwner, Observer { render(it) })
        (activity as MainActivity).navigationItemSelected.observe(viewLifecycleOwner
        ) { setChosenTimeControl(it) }

        binding.showMenuIv.setOnClickListener { (openDrawer()) }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getGame()
    }


    override fun onPause() {
        super.onPause()
        viewModel.saveGame()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeUi(binding: FragmentHomeBinding) {

        binding.topButtonView.setOnClickListener { viewModel.clickOnTopButtonView() }
        binding.deleteIb.setOnClickListener { viewModel.clickOnPause() }
        binding.bottomButtonView.setOnClickListener { viewModel.clickOnBottomButtonView() }

        viewModel.gameState.observe(viewLifecycleOwner) {
            setButtonsBG(binding.topButtonView, it.topButtonBG)
            setButtonsBG(binding.bottomButtonView, it.bottomButtonBG)
            convertButtonTextColor(binding.topTimeTv, it.isBottomPressedFirst)
            convertButtonTextColor(binding.bottomTimeTv, it.isBottomPressedFirst)
            convertTimeControlFromGame(binding.timeTv, it.timeControl)
            convertChangePausedIcon(binding.deleteIb, it.changedToPauseIcon)
            convertRestClockTime(binding.bottomTimeTv, it.restTimeBottom)
            convertRestClockTime(binding.topTimeTv, it.restTimeTop)
        }

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


    private fun openDrawer() {
        if (isClickedSingle()) return
        val mainActivity = activity as MainActivity
        mainActivity.openDrawer()
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
