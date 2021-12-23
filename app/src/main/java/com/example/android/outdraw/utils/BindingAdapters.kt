package com.example.android.outdraw.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.outdraw.R
import com.example.android.outdraw.database.Painting

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
                    .error(R.drawable.ic_broken_image)
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
                    .error(R.drawable.back_3_1_wide)
            )
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Painting>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}
