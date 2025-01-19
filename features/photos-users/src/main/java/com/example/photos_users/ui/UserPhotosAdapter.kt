package com.example.photos_users.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.photos_users.data.UserPhotosModel
import com.example.photos_users.databinding.UserItemBinding

class UserPhotosAdapter(private val onItemClick: (String) -> Unit) :
    ListAdapter<UserPhotosModel, UserPhotosAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: UserPhotosModel) {
            with(binding) {
                userPhotoDescription.text = model.photoDescription
                userPhotoImage.load(model.pathUrl)
            }
            itemView.setOnClickListener {
                onItemClick(model.photoId ?: "0")
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserPhotosAdapter.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val binding = UserItemBinding.inflate(inflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: UserPhotosAdapter.ViewHolder, p1: Int) {
        p0.bind(getItem(p1))
    }
}

private class DiffCallback : DiffUtil.ItemCallback<UserPhotosModel>() {
    private val payLoad: Any = Any()
    override fun areItemsTheSame(p0: UserPhotosModel, p1: UserPhotosModel): Boolean {
        return p0.photoId == p1.photoId
    }

    override fun areContentsTheSame(p0: UserPhotosModel, p1: UserPhotosModel): Boolean {
        return p0 == p1
    }

    override fun getChangePayload(oldItem: UserPhotosModel, newItem: UserPhotosModel): Any {
        return payLoad
    }
}

