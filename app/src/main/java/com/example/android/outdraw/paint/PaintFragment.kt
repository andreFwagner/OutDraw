package com.example.android.outdraw.paint

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.android.outdraw.R
import com.example.android.outdraw.databinding.FragmentPaintBinding
import com.example.android.outdraw.home.HomeViewModel
import com.udacity.project4.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaintFragment : BaseFragment() {

    override val _viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentPaintBinding
    private lateinit var myCanvasView: MyCanvasView
    private var animationSet = AnimatorSet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_paint, container, false
        )

        myCanvasView = MyCanvasView(requireContext())

        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)

        binding.paintConstraint.addView(myCanvasView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.paintBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.paintSaveButton.setOnClickListener {
            _viewModel.showToast.value = getString(R.string.painting_saved)
            // TODO implement saving
            clearCanvas()
        }

        binding.paintClearButton.setOnClickListener {
            clearCanvas()
        }
    }

    private fun clearCanvas() {
        val clearCanvasAnimation = ObjectAnimator.ofFloat(myCanvasView, View.ALPHA, 0.0f)
        clearCanvasAnimation.duration = 2000
        clearCanvasAnimation.start()

        clearCanvasAnimation.doOnEnd {
            binding.paintConstraint.removeView(myCanvasView)

            val newCanvasView = MyCanvasView(requireContext())
            newCanvasView.contentDescription = getString(R.string.canvasContentDescription)
            binding.paintConstraint.addView(newCanvasView)
            myCanvasView = newCanvasView
        }
    }

    fun fadeUIIn() {
        animationSet.cancel()
        animationSet = AnimatorSet()
        val backButtonAnimator = ObjectAnimator.ofFloat(binding.paintBackButton, View.ALPHA, 0.7f)
        val saveButtonAnimator = ObjectAnimator.ofFloat(binding.paintSaveButton, View.ALPHA, 0.7f)
        val clearButtonAnimator = ObjectAnimator.ofFloat(binding.paintClearButton, View.ALPHA, 0.7f)
        animationSet.playTogether(backButtonAnimator, saveButtonAnimator, clearButtonAnimator)
        animationSet.duration = 1500
        animationSet.startDelay = 1500
        animationSet.start()

        animationSet.doOnEnd {
            binding.paintBackButton.isClickable = true
            binding.paintSaveButton.isClickable = true
            binding.paintClearButton.isClickable = true
        }
    }

    fun fadeUIOut() {
        animationSet.cancel()
        animationSet = AnimatorSet()
        val backButtonAnimator = ObjectAnimator.ofFloat(binding.paintBackButton, View.ALPHA, 0.0f)
        val saveButtonAnimator = ObjectAnimator.ofFloat(binding.paintSaveButton, View.ALPHA, 0.0f)
        val clearButtonAnimator = ObjectAnimator.ofFloat(binding.paintClearButton, View.ALPHA, 0.0f)
        animationSet.playTogether(backButtonAnimator, saveButtonAnimator, clearButtonAnimator)
        animationSet.duration = 500
        animationSet.start()

        binding.paintBackButton.isClickable = false
        binding.paintSaveButton.isClickable = false
        binding.paintClearButton.isClickable = false
    }
}
