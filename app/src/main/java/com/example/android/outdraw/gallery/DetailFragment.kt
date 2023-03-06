package com.example.android.outdraw.gallery

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import com.example.android.outdraw.R
import com.example.android.outdraw.database.Painting
import com.example.android.outdraw.databinding.FragmentDetailBinding
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * DetailFragment handling the Navigation and ShareIntent of the DetailScreen
 */

class DetailFragment : BaseFragment() {

    override val _viewModel: GalleryViewModel by viewModel()
    private lateinit var binding: FragmentDetailBinding
    private lateinit var painting: Painting

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        painting = DetailFragmentArgs.fromBundle(requireArguments()).selectedPainting

        _viewModel.setSelectedPainting(painting)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailBackButton.setOnClickListener {
            _viewModel.navigationCommand.postValue(NavigationCommand.Back)
        }

        binding.detailShareButton.setOnClickListener {
            onShare()
        }

        binding.detailDeleteButton.setOnClickListener {
            AlertDialog.Builder(activity)
                .setMessage(R.string.delete_dialog)
                .setPositiveButton(
                    R.string.delete_dialog_yes,
                    DialogInterface.OnClickListener { dialog, which ->
                        _viewModel.deletePainting(painting)
                    }
                )
                .setNegativeButton(
                    R.string.delete_dialog_no,
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    }
                )
                .create().show()
        }
    }

    private fun onShare() {
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setType("image/*")
            .setText(getString(R.string.share_text))
            .setStream(painting.image.toUri())
            .intent

        try {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_where)))
        } catch (ex: ActivityNotFoundException) {
            _viewModel.showToast.value = getString(R.string.sharing_failed)
        }
    }
}
