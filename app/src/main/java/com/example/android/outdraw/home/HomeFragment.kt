package com.example.android.outdraw.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.android.outdraw.R
import com.example.android.outdraw.base.BaseFragment
import com.example.android.outdraw.base.NavigationCommand
import com.example.android.outdraw.databinding.FragmentHomeBinding
import com.example.android.outdraw.gallery.GalleryViewModel
import com.example.android.outdraw.utils.bindArtPiece
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * HomeFragment handling Navigation and Animations of the HomeScreen
 */

class HomeFragment : BaseFragment() {

    override val _viewModel: GalleryViewModel by viewModel()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false,
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

        binding.homeAbout.setOnClickListener {
            fadeAbout()
            println(_viewModel.artPiece.value)
        }

        binding.homeStartButton.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionHomeFragmentToPaintFragment())
        }
        binding.homeGalleryButton.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionHomeFragmentToGalleryFragment())
        }
        _viewModel.artPiece.observe(
            viewLifecycleOwner,
        ) {
            if (it != null) {
                bindArtPiece(binding.homeArtView, it.primaryImage)
            }
        }
    }

    private fun fadeAbout() {
        if (binding.homeAboutFrame.alpha == 0.0f) {
            val anim = ObjectAnimator.ofFloat(binding.homeAboutFrame, View.ALPHA, 0.9f)
            anim.duration = 400
            anim.start()
            binding.homeAbout.text = getString(R.string.back_button_text)
        } else if (binding.homeAboutFrame.alpha == 0.9f) {
            val anim = ObjectAnimator.ofFloat(binding.homeAboutFrame, View.ALPHA, 0.0f)
            anim.duration = 400
            anim.start()
            binding.homeAbout.text = getString(R.string.about_button_text)
        }
    }

    private fun fadeArtPieceOut() {
        val anim = ObjectAnimator.ofFloat(binding.homeArtView, View.ALPHA, 0f)
        anim.duration = 100
        anim.start()
    }

    private fun navigateTo(direction: NavDirections) {
        val listener = object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
            ) {
                val fadeBg = ObjectAnimator.ofFloat(binding.homeConstraint, View.ALPHA, 0f)
                fadeBg.duration = 700
                fadeBg.start()
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float,
            ) {
                // nothing
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                binding.homeConstraint.removeTransitionListener(this)
//                _viewModel.navigationCommand.postValue(
//                    NavigationCommand.To(
//                        direction,
//                    ),
//                )
                findNavController().navigate(direction)
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float,
            ) {
                // nothing
            }
        }
        binding.homeConstraint.setTransitionListener(listener)
        binding.homeConstraint.transitionToStart()
        fadeArtPieceOut()
    }
}
