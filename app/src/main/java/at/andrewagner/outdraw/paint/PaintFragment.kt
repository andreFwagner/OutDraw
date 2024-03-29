package at.andrewagner.outdraw.paint

import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import at.andrewagner.outdraw.base.BaseFragment
import at.andrewagner.outdraw.gallery.GalleryViewModel
import com.example.android.outdraw.R
import com.example.android.outdraw.databinding.FragmentPaintBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * PaintFragment loading MyCanvasView to paint, handling Navigation and Animation of UI and checks Permissions for saving
 */

const val REQUEST_STORAGE_PERMISSION = 1

class PaintFragment : BaseFragment() {

    override val _viewModel: GalleryViewModel by viewModel()
    private lateinit var binding: FragmentPaintBinding
    private lateinit var myCanvasView: MyCanvasView
    private var animator = ObjectAnimator()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_paint,
            container,
            false,
        )

        myCanvasView = MyCanvasView(requireContext())
        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)

        binding.paintConstraint.addView(myCanvasView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback {
            // nothing
        }

        binding.paintBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.paintSaveButton.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                _viewModel.savePainting(myCanvasView.saveBitmap())
                clearCanvas()
            } else {
                checkPermissionsBeforeSave()
            }
        }

        binding.paintClearButton.setOnClickListener {
            clearCanvas()
        }
    }

    @Suppress("DEPRECATION")
    private fun checkPermissionsBeforeSave() {
        if (isPermissionGranted()) {
            _viewModel.savePainting(myCanvasView.saveBitmap())
            clearCanvas()
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION,
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkPermissionsBeforeSave()
        } else {
            _viewModel.showToast.value = "Storage Permission is needed to save your paintings!"
            @Suppress("DEPRECATION")
            requestPermissions(
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION,
            )
        }
    }

    private fun clearCanvas() {
        val clearCanvasAnimation = ObjectAnimator.ofFloat(myCanvasView, View.ALPHA, 0.0f)
        clearCanvasAnimation.duration = 500
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
        animator.cancel()
        animator = ObjectAnimator.ofFloat(binding.paintControlCard, View.TRANSLATION_Y, 0f)
        animator.duration = 700
        animator.startDelay = 800
        animator.start()

        animator.doOnEnd {
            binding.paintBackButton.isClickable = true
            binding.paintSaveButton.isClickable = true
            binding.paintClearButton.isClickable = true
        }
    }

    fun fadeUIOut() {
        animator.cancel()
        animator = ObjectAnimator.ofFloat(binding.paintControlCard, View.TRANSLATION_Y, 220f)
        animator.duration = 350
        animator.start()

        binding.paintBackButton.isClickable = false
        binding.paintSaveButton.isClickable = false
        binding.paintClearButton.isClickable = false
    }

    private fun isPermissionGranted(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        } == PackageManager.PERMISSION_GRANTED
    }
}
