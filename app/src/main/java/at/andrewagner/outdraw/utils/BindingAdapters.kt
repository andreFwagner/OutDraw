package at.andrewagner.outdraw.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import at.andrewagner.outdraw.database.Painting
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.outdraw.R

/**
 * BindingAdapters for RecyclerView and ImageViews using Glide
 */

@BindingAdapter("imagePath")
fun bindImage(imgView: ImageView, imgPath: String?) {
    imgPath?.let {
        Glide.with(imgView.context)
            .load(imgPath)
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_broken_image),
            )
            .into(imgView)
    }
}

@BindingAdapter("artPiecePath")
fun bindArtPiece(imgView: ImageView, imgPath: String?) {
    imgPath?.let {
        Glide.with(imgView.context)
            .load(imgPath)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.artpiece_placeholder)
                    .error(R.drawable.artpiece_placeholder),
            )
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Painting>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}
