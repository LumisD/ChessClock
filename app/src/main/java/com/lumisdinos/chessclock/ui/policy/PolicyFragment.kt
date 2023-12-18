package com.lumisdinos.chessclock.ui.policy

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lumisdinos.chessclock.R
import com.lumisdinos.chessclock.databinding.FragmentPrivacyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyFragment : Fragment() {

    private var _binding: FragmentPrivacyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PolicyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyBinding.inflate(inflater, container, false)
        val view = binding.root
        setHasOptionsMenu(true)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linkTextView.movementMethod = LinkMovementMethod.getInstance()
        binding.backImageView.setOnClickListener {
            findNavController().navigate(R.id.action_privacy_policy_to_home)
        }
    }
}