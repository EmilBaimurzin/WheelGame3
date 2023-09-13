package com.wheeler.game.ui.wheeler

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wheeler.game.R
import com.wheeler.game.core.library.GameFragment
import com.wheeler.game.databinding.FragmentWheelerBinding

class FragmentWheeler: GameFragment<FragmentWheelerBinding>(FragmentWheelerBinding::inflate) {
    private val viewModel: WheelerViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spin.setOnClickListener {
            if (!viewModel.isSpinning && viewModel.state.value!! == 1) {
                viewModel.spin()
            }
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.restart.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentWheeler)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.wheelState.setImageResource(when(it) {
                1 -> R.drawable.text01
                2 -> R.drawable.text02
                3 -> R.drawable.text03
                4 -> R.drawable.text04
                5 -> R.drawable.text05
                6 -> R.drawable.text06
                7 -> R.drawable.text07
                else -> R.drawable.text08
            })

            if (it == 1 && viewModel.spins.value!! == 0 && viewModel.gameState) {
                viewModel.gameState = false
                findNavController().navigate(FragmentWheelerDirections.actionFragmentWheelerToFragmentEnd(viewModel.scores.value!!))
            }
        }

        viewModel.stateText.observe(viewLifecycleOwner) {
            binding.wheelStateText.text = it
        }

        viewModel.scores.observe(viewLifecycleOwner) {
            binding.pointsText.text = it.toString()
        }

        viewModel.spins.observe(viewLifecycleOwner) {
            binding.ticketsLayout.removeAllViews()
            binding.spinsAmount.text = it.toString()
            repeat(it) {
                val ticketView = ImageView(requireContext())
                ticketView.layoutParams = LinearLayout.LayoutParams(dpToPx(70), dpToPx(42)).apply {
                    marginStart = dpToPx(2)
                }
                ticketView.setImageResource(R.drawable.ticket)
                binding.ticketsLayout.addView(ticketView)
            }
        }

        viewModel.bigWheelRotation.observe(viewLifecycleOwner) {
            binding.bigWheel.rotation = -it + 10
        }

        viewModel.smallWheelRotation.observe(viewLifecycleOwner) {
            binding.smallWheel.rotation = -it + 15
        }
    }
}