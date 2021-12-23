package com.example.android.outdraw.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.android.outdraw.databinding.FragmentGalleryBinding
import com.example.android.outdraw.utils.PhotoGridAdapter
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * GalleryFragment handling Navigation of the GalleryScreen (incl. RecyclerView)
 */

class GalleryFragment : BaseFragment() {

    override val _viewModel: GalleryViewModel by viewModel()
    private lateinit var binding: FragmentGalleryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater)
        binding.viewmodel = _viewModel
        binding.galleryGrid.adapter = PhotoGridAdapter(
            PhotoGridAdapter.OnClickListener {
                _viewModel.getPaintingDetail(it)
            }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this

        binding.galleryGrid.adapter?.notifyDataSetChanged()

        binding.galleryBackButton.setOnClickListener {
            _viewModel.navigationCommand.postValue(NavigationCommand.Back)
        }

        _viewModel.navigateToSelectedPainting.observe(
            viewLifecycleOwner,
            Observer {
                if (it != null) {
                    _viewModel.navigationCommand.postValue(
                        NavigationCommand.To(
                            GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(it)
                        )
                    )
                    _viewModel.displayPaintingDetailComplete()
                }
            }
        )
    }
}
