package com.example.android.outdraw.home

import android.animation.ObjectAnimator
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import com.example.android.outdraw.R
import com.example.android.outdraw.databinding.FragmentHomeBinding
import com.example.android.outdraw.gallery.GalleryViewModel
import com.example.android.outdraw.utils.bindArtPiece
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
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
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )

        // unlocks Orientation in case you come from PaintingFragment
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

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
            Observer {
                if (it != null) {
                    bindArtPiece(binding.homeArtView!!, it.primaryImage)
                }
            }
        )
    }

    private fun fadeAbout() {
        if (binding.homeAboutFrame?.alpha == 0.0f) {
            val anim = ObjectAnimator.ofFloat(binding.homeAboutFrame, View.ALPHA, 0.9f)
            anim.duration = 400
            anim.start()
            binding.homeAbout.setImageResource(R.drawable.ic_clear)
        } else if (binding.homeAboutFrame?.alpha == 0.9f) {
            val anim = ObjectAnimator.ofFloat(binding.homeAboutFrame, View.ALPHA, 0.0f)
            anim.duration = 400
            anim.start()
            binding.homeAbout.setImageResource(android.R.drawable.ic_menu_help)
        }
    }

    private fun fadeArtPieceOut() {
        val anim = ObjectAnimator.ofFloat(binding.homeArtView, View.ALPHA, 0f)
        anim.duration = 300
        anim.start()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            // sets the background to a wider version of the image
            binding.homeBackground.setImageResource(R.drawable.back_3_1_wide)
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            binding.homeBackground.setImageResource(R.drawable.back_3_1)
        }
    }

    private fun navigateTo(direction: NavDirections) {
        val listener = object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                // nothing
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                // nothing
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                _viewModel.navigationCommand.postValue(
                    NavigationCommand.To(
                        direction
                    )
                )
                binding.homeConstraint.removeTransitionListener(this)
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                // nothing
            }
        }
        binding.homeConstraint.transitionToStart()
        fadeArtPieceOut()
        binding.homeConstraint.setTransitionListener(listener)
    }
}
