package com.example.android.outdraw.gallery

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.example.android.outdraw.R
import com.example.android.outdraw.database.Painting
import com.example.android.outdraw.databinding.FragmentDetailBinding
import com.udacity.project4.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            findNavController().navigateUp()
        }

        binding.detailDeleteButton.setOnClickListener {
            AlertDialog.Builder(activity)
                .setMessage(R.string.delete_dialog)
                .setPositiveButton(
                    "yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        _viewModel.deletePainting(painting)
                    }
                )
                .setNegativeButton(
                    "no",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.cancel()
                    }
                )
                .create().show()
        }
    }
}
