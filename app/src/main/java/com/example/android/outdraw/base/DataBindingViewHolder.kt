package com.udacity.project4.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.android.outdraw.BR

/**
 * View Holder for the Recycler View to bind the data item to the UI
 */
class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T) {
     //TODO uncomment after databinding   binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}
