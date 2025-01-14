package com.example.photos_random.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.photos_random.databinding.ItemLayoutBinding
import data.RandomPhotoModel

class RandomPhotoAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<RandomPhotoModel, RandomPhotoAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: RandomPhotoModel) {
            with(binding) {
                randomPhotoDescription.text = model.description
                randomPhotoImage.load(model.pathUrl)
            }
            itemView.setOnClickListener {
                onItemClick(model.id ?: "0")
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RandomPhotoAdapter.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val binding = ItemLayoutBinding.inflate(inflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: RandomPhotoAdapter.ViewHolder, p1: Int) {
        p0.bind(getItem(p1))
    }
}

private class DiffCallback : DiffUtil.ItemCallback<RandomPhotoModel>() {
    private val payLoad: Any = Any()
    override fun areItemsTheSame(p0: RandomPhotoModel, p1: RandomPhotoModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: RandomPhotoModel, p1: RandomPhotoModel): Boolean {
        return p0 == p1
    }

    override fun getChangePayload(oldItem: RandomPhotoModel, newItem: RandomPhotoModel): Any {
        return payLoad
    }

}
