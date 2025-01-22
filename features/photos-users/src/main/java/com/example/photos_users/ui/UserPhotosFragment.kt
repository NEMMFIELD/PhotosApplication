package com.example.photos_users.ui

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
import com.example.photos_users.R
import com.example.photos_users.databinding.FragmentUserBinding
import com.example.state.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserPhotosFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var userPhotosRecyclerView: RecyclerView? = null
    private var userPhotosAdapter: UserPhotosAdapter? = null
    private val userPhotosViewModel: UserPhotosViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        collectUserPhotos()
        binding.buttonBackUser.setOnClickListener { findNavController().navigateUp() }
        val uri = requireActivity().intent?.data
        uri?.let {
            userPhotosViewModel.name = it.getQueryParameter("name")
        }
        binding.toolbarUsername.text = userPhotosViewModel.name

        userPhotosRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !userPhotosViewModel.isLoading && isLastItemVisible(recyclerView)) {
                    userPhotosViewModel.loadNextPage()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val spanCount = if (activity?.resources?.configuration?.orientation !=
            Configuration.ORIENTATION_PORTRAIT
        ) 3 else 2
        userPhotosRecyclerView = binding.userList
        userPhotosRecyclerView?.layoutManager = GridLayoutManager(requireContext(), spanCount)
        userPhotosAdapter = UserPhotosAdapter() { itemId ->
            val request = NavDeepLinkRequest.Builder
                .fromUri(requireContext().getString(R.string.nav_deep_link, itemId).toUri())
                .build()
            findNavController().navigate(request)
        }
        userPhotosRecyclerView?.adapter = userPhotosAdapter
    }

    private fun isLastItemVisible(recyclerView: RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager as? GridLayoutManager
        val lastVisibleItemPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
        val totalItemCount = layoutManager?.itemCount ?: 0
        return lastVisibleItemPosition >= totalItemCount - 1
    }

    private fun collectUserPhotos() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userPhotosViewModel.postUserPhotos.collect { state ->
                    when (state) {
                        is State.Success -> {
                            userPhotosAdapter?.submitList(state.data)
                        }

                        is State.Failure -> {
                            Log.d(
                                "Error in UserPhotosFragment",
                                state.message.message ?: "null error"
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        userPhotosAdapter = null
        userPhotosRecyclerView = null
        super.onDestroyView()
    }
}

