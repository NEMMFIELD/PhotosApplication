package com.example.photos.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.photos.search.R
import com.example.photos.search.databinding.FragmentPhotosSearchBinding
import com.example.state.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchPhotosFragment : Fragment() {

    private var _binding: FragmentPhotosSearchBinding? = null
    private val binding get() = _binding!!
    private var searchPhotosAdapter: SearchPhotosAdapter? = null
    private var searchRecyclerView: RecyclerView? = null
    private val searchViewModel: SearchPhotosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        searchPhotosCollect()
        binding.searchViewEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchViewModel.searchPhotos(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        searchRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? GridLayoutManager
                val totalItemCount = layoutManager?.itemCount ?: 0
                val lastVisibleItem = layoutManager?.findLastVisibleItemPosition() ?: 0
                // Проверка: если до конца списка осталось меньше 5 элементов, загружаем данные
                if (!searchViewModel.isLoading && lastVisibleItem + 5 >= totalItemCount) {
                    searchViewModel.loadMorePhotos()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        searchRecyclerView = binding.searchPhotosRecyclerView
        searchRecyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        searchPhotosAdapter = SearchPhotosAdapter() { itemId ->
            val request = NavDeepLinkRequest.Builder
                .fromUri(requireContext().getString(R.string.nav_deep_link_details, itemId).toUri())
                .build()
            findNavController().navigate(request)

        }
        searchRecyclerView?.adapter = searchPhotosAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchPhotosCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.allPhotos.collect { state ->
                    when (state) {
                        is State.Success -> {
                            searchPhotosAdapter?.submitList(state.data)
                            searchPhotosAdapter?.notifyDataSetChanged()
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

    companion object {
        @JvmStatic
        fun newInstance() = SearchPhotosFragment()
    }

    override fun onDestroyView() {
        _binding = null
        searchRecyclerView = null
        searchPhotosAdapter = null
        super.onDestroyView()

    }
}

