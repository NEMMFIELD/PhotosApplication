package com.example.photos.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.photos.search.data.PhotoModel
import com.example.photos.search.databinding.SearchItemLayoutBinding

class SearchPhotosAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<PhotoModel, SearchPhotosAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(private val binding: SearchItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: PhotoModel) {
            with(binding) {
                searchPhotoDescription.text = model.description
                searchPhotoImage.load(model.pathUrl)
            }
            itemView.setOnClickListener {
                onItemClick(model.id ?: "0")
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchPhotosAdapter.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val binding = SearchItemLayoutBinding.inflate(inflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: SearchPhotosAdapter.ViewHolder, p1: Int) {
        p0.bind(getItem(p1))
    }
}

private class DiffCallback : DiffUtil.ItemCallback<PhotoModel>() {
    private val payLoad: Any = Any()
    override fun areItemsTheSame(p0: PhotoModel, p1: PhotoModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: PhotoModel, p1: PhotoModel): Boolean {
        return p0 == p1
    }

    override fun getChangePayload(oldItem: PhotoModel, newItem: PhotoModel): Any {
        return payLoad
    }

}
