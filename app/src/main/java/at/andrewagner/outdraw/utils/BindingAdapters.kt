package at.andrewagner.outdraw.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import at.andrewagner.outdraw.database.Painting
import coil.load
import com.example.android.outdraw.R

/**
 * BindingAdapters for RecyclerView and ImageViews using Glide
 */

@BindingAdapter("imagePath")
fun bindImage(imgView: ImageView, imgPath: String?) {
    imgView.load(imgPath) {
        crossfade(true)
        error(R.drawable.ic_broken_image)
    }
}

@BindingAdapter("artPiecePath")
fun bindArtPiece(imgView: ImageView, imgPath: String?) {
    imgView.load(imgPath) {
        crossfade(true)
        placeholder(R.drawable.artpiece_placeholder)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Painting>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}
