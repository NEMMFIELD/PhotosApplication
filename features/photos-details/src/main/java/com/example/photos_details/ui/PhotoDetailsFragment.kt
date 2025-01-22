package com.example.photos_details.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import coil3.load
import com.example.photos_details.R
import com.example.photos_details.databinding.FragmentPhotoDetailsBinding
import com.example.photos_details.datali.PhotoDetailsModel
import com.example.state.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {
    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding
    private val photoDetailsViewModel: PhotoDetailsViewModel by viewModels()
    private var collectPhotoDetailsJob: Job? = null
    private var collectDownloadPhotoJob: Job? = null
    private lateinit var receiver: PhotoDownloadReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        receiver = PhotoDownloadReceiver(view as View)
        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        val intentFilter = IntentFilter("com.example.LOCAL_DOWNLOAD_COMPLETE")
        localBroadcastManager.registerReceiver(receiver, intentFilter)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.detailsLikedUser?.setOnClickListener {
            toggleLikePhoto()
        }

        collectPhotoDetails()
        collectDownLoadedPhoto()

        binding?.buttonBack?.setOnClickListener { findNavController().navigateUp() }
        binding?.downloadPhotoImg?.setOnClickListener {
            downLoadPhoto(photoDetailsViewModel.photoId ?: "")
            sendLocalBroadcast()
        }

        binding?.user?.setOnClickListener {
            navigateToUserFragment()
        }
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
        collectPhotoDetailsJob = viewLifecycleOwner.lifecycleScope.launch {
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
                                this?.user?.text = state.data.name
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


    private fun collectDownLoadedPhoto() {
        collectDownloadPhotoJob = viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoDetailsViewModel.downloadedPhoto.collect { state ->
                    when (state) {
                        is State.Success -> Log.d("Download Url", state.data)
                        is State.Failure -> Log.d("Error during download", "Error ${state.message}")
                        else -> {}
                    }
                }
            }
        }
    }

    private fun downLoadPhoto(photoId: String) {
        photoDetailsViewModel.downLoadPhoto(photoId)
    }

    private fun navigateToUserFragment() {
        val request = NavDeepLinkRequest.Builder
            .fromUri(
                requireContext().getString(
                    R.string.details_nav_deep_link,
                    photoDetailsViewModel.username,
                    photoDetailsViewModel.name
                ).toUri()
            )
            .build()
        findNavController().navigate(request)
    }


    private fun sendLocalBroadcast() {
        val intent = Intent("com.example.LOCAL_DOWNLOAD_COMPLETE").apply {
            putExtra("MESSAGE", "Image ${photoDetailsViewModel.photoId} is downloaded!")
        }
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    }

    override fun onDestroyView() {
        receiver.clearRootView()
        localBroadcastManager.unregisterReceiver(receiver)
        _binding = null
        collectPhotoDetailsJob?.cancel()  // Отмена сбора данных
        collectDownloadPhotoJob?.cancel() // Отмена загрузки
        super.onDestroyView()
    }
}
