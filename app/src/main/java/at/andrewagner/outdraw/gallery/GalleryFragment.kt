package at.andrewagner.outdraw.gallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import at.andrewagner.outdraw.base.BaseFragment
import at.andrewagner.outdraw.utils.PhotoGridAdapter
import com.example.android.outdraw.databinding.FragmentGalleryBinding
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
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater)
        binding.viewmodel = _viewModel
        binding.galleryGrid.adapter = PhotoGridAdapter(
            PhotoGridAdapter.OnClickListener {
                _viewModel.getPaintingDetail(it)
            },
        )

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this

        binding.galleryGrid.adapter?.notifyDataSetChanged()

        binding.galleryBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        _viewModel.navigateToSelectedPainting.observe(
            viewLifecycleOwner,
        ) {
            if (it != null) {
                findNavController()
                    .navigate(GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(it))
                _viewModel.displayPaintingDetailComplete()
            }
        }
    }
}
