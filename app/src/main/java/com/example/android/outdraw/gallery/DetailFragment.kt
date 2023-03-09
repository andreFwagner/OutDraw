package com.example.android.outdraw.gallery

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
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
        Log.d("DetailFragment", "uri: ${painting.image.toUri()}")

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.clipData = ClipData.newRawUri("", painting.image.toUri())
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text))
        intent.putExtra(Intent.EXTRA_STREAM, painting.image.toUri())
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_where)))
        } catch (ex: ActivityNotFoundException) {
            _viewModel.showToast.value = getString(R.string.sharing_failed)
        }
    }
}
