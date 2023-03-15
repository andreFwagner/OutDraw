/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.outdraw.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.outdraw.database.Painting
import com.example.android.outdraw.databinding.GridViewItemBinding

/**
 * ListAdapter and ViewHolder for RecyclerView
 */

class PhotoGridAdapter(private val onClickListener: OnClickListener) : ListAdapter<Painting, PhotoGridAdapter.PaintingViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<Painting>() {
        override fun areItemsTheSame(oldItem: Painting, newItem: Painting): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Painting, newItem: Painting): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaintingViewHolder {
        return PaintingViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PaintingViewHolder, position: Int) {
        val painting = getItem(position)
        holder.bind(painting)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(painting)
        }
    }

    class PaintingViewHolder(private var binding: GridViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(painting: Painting) {
            binding.painting = painting
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (painting: Painting) -> Unit) {
        fun onClick(painting: Painting) = clickListener(painting)
    }
}
