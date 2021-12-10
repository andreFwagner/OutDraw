package com.example.android.outdraw.paint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.outdraw.R
import com.example.android.outdraw.databinding.FragmentPaintBinding
import com.example.android.outdraw.home.HomeViewModel
import com.udacity.project4.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaintFragment : BaseFragment() {

    override val _viewModel: HomeViewModel by viewModel()
    private lateinit var binding: FragmentPaintBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_paint, container, false
        )

        val myCanvasView = MyCanvasView(requireContext())

        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)

        binding.paintConstraint.addView(myCanvasView)

        return binding.root
    }
}
