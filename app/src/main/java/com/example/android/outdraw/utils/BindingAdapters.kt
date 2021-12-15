package com.example.android.outdraw.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.outdraw.R
import com.example.android.outdraw.database.Painting
import java.io.File

@BindingAdapter("imagePath")
fun bindImage(imgView: ImageView, imgPath: String?) {
    imgPath?.let {
        val imgFile = File(imgPath)
        Glide.with(imgView.context)
            .load(imgFile.absolutePath)
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Painting>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}
