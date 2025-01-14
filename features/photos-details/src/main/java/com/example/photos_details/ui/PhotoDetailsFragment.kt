package com.example.photos_details.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil3.load
import com.example.photos_details.R
import com.example.photos_details.databinding.FragmentPhotoDetailsBinding
import com.example.state.State
import com.example.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {
    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding
    private val photoDetailsViewModel: PhotoDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.detailsLikedUser?.setOnClickListener {
            toggleLikePhoto()
        }
        collectPhotoDetails()
        binding?.buttonBack?.setOnClickListener { findNavController().navigateUp() }
    }

    private fun toggleLikePhoto() {
        val photoId = photoDetailsViewModel.photoId
        val token = photoDetailsViewModel.token

        if (!photoId.isNullOrEmpty() && !token.isNullOrEmpty()) {
            photoDetailsViewModel.toggleLike(photoId, token)
        } else {
            Log.e("PhotoDetails", "Photo ID or token is missing")
        }
    }

    private fun collectPhotoDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoDetailsViewModel.postPhoto.collect { state ->
                    when (state) {
                        is State.Success -> {
                            with(binding) {
                                this?.detailsDescription?.text = state.data.description
                                this?.detailsPhoto?.load(state.data.pathUrl)
                                this?.detailsLocation?.text = state.data.location.toString()
                                this?.detailsDownloads?.text = requireContext().getString(
                                    R.string.downloads_format, state.data.downloads
                                )
                                this?.detailsLikes?.text = requireContext().getString(
                                    R.string.likes_format, state.data.likes
                                )
                                this?.detailsUserPortfolio?.text =
                                    state.data.userPortfolio.toString()

                                if (state.data.likedByUser == true) {
                                    this?.detailsLikedUser?.setImageResource(R.drawable.hearton)
                                } else {
                                    this?.detailsLikedUser?.setImageResource(R.drawable.heartoff)
                                }
                            }
                        }

                        is State.Failure -> {
                            Log.d("Error in PhotoDetails", "Error ${state.message}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) = PhotoDetailsFragment()
    }
}
