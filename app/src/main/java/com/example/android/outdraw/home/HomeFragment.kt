package com.example.android.outdraw.home

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDirections
import com.example.android.outdraw.R
import com.example.android.outdraw.databinding.FragmentHomeBinding
import com.example.android.outdraw.gallery.GalleryViewModel
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this

        binding.homeStartButton.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionHomeFragmentToPaintFragment())
        }
        binding.homeGalleryButton.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionHomeFragmentToGalleryFragment())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
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
        binding.homeConstraint.setTransitionListener(listener)
    }
}
