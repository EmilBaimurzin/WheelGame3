package com.wheeler.game.ui.game_over

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wheeler.game.R
import com.wheeler.game.databinding.FragmentEndBinding
import com.wheeler.game.ui.other.ViewBindingFragment

class FragmentEnd: ViewBindingFragment<FragmentEndBinding>(FragmentEndBinding::inflate) {
    private val args: FragmentEndArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pointsText.text = args.scores.toString()
        binding.back.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }
        binding.replay.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentWheeler)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.fragmentMain, false, false)
            }
        })
    }
}