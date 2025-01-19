package com.example.photos_random.ui

import android.content.res.Configuration
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
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

import com.example.photos_random.R
import com.example.photos_random.databinding.FragmentRandomPhotoBinding
import com.example.state.State
import com.example.workmanager.RandomPhotoWorkerRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RandomPhotoFragment : Fragment() {

    private var _binding: FragmentRandomPhotoBinding? = null
    private val binding get() = _binding
    private var randomPhotoAdapter: RandomPhotoAdapter? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: RandomPhotoViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRandomPhotoBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        randomPhotosCollect()
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "RandomWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            RandomPhotoWorkerRepository().periodicWork
        )
    }


    private fun setupRecyclerView() {
        val spanCount = if (activity?.resources?.configuration?.orientation !=
            Configuration.ORIENTATION_PORTRAIT
        ) 3 else 2
        recyclerView = binding!!.randomPhotoRecyclerView
        recyclerView?.layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView?.setHasFixedSize(true)
        randomPhotoAdapter = RandomPhotoAdapter() { itemId ->
            val request = NavDeepLinkRequest.Builder
                .fromUri(requireContext().getString(R.string.nav_deep_link, itemId).toUri())
                .build()
            findNavController().navigate(request)

        }
        recyclerView?.adapter = randomPhotoAdapter
    }

    private fun randomPhotosCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postRandomPhoto.collect { state ->
                    when (state) {
                        is State.Success -> {
                            randomPhotoAdapter?.submitList(state.data)
                        }

                        is State.Failure -> {
                            Log.d("Error", "During load random photos ${state.message}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    fun refreshData() {
        viewModel.isDataLoaded = false // Сбрасываем флаг
        viewModel.loadRandomPhoto(RANDOM_PHOTO_COUNT)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            RandomPhotoFragment()
    }

    override fun onDestroyView() {
        _binding = null
        recyclerView = null
        randomPhotoAdapter = null
        super.onDestroyView()
    }
}
