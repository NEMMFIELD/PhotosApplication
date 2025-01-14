package com.example.authorization.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.authorization.databinding.FragmentPhotoAuthorizationBinding
import com.example.photos_random.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoAuthorizationFragment : Fragment() {
    private var _binding: FragmentPhotoAuthorizationBinding? = null
    private val binding get() = _binding
    private val photoAuthViewModel: PhotoAuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoAuthorizationBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = photoAuthViewModel.getStoredToken()
        if (token != null) {
            Toast.makeText(requireContext(), "Токен уже сохранён: $token", Toast.LENGTH_SHORT)
                .show()
            navigateWithDeepLink(findNavController(), "android-app://com.example.photos_random.ui")
        }
        binding?.btnLogin?.setOnClickListener {
            val authUrl = buildAuthUrl()
            openAuthUrl(authUrl)
        }
        observeViewModel()

        binding?.submitCodeButton?.setOnClickListener {
            val code = binding?.codeInput?.text.toString()
            if (code.isNotEmpty()) {
                photoAuthViewModel.authenticate(code) // Передать код в ViewModel
                navigateWithDeepLink(
                    findNavController(),
                    "android-app://com.example.photos_random.ui"
                )
            } else {
                Toast.makeText(context, "Code cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Формируем URL для авторизации
    private fun buildAuthUrl(): String {
        val clientId = BuildConfig.ACCESS_KEY
        val redirectUri = "urn:ietf:wg:oauth:2.0:oob"
        val responseType = "code"
        return "https://unsplash.com/oauth/authorize?" +
                "client_id=$clientId&redirect_uri=${Uri.encode(redirectUri)}&response_type=$responseType&scope=public+write_likes"
    }

    //Открываем URL в браузере
    private fun openAuthUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    //Подписываемся на изменения состояния авторизации
    private fun observeViewModel() {
        photoAuthViewModel.authState.observe(viewLifecycleOwner) { token ->
            token?.let {
                Toast.makeText(requireContext(), "Токен получен: $token", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateWithDeepLink(navController: NavController, deepLinkUri: String) {
        val uri = Uri.parse(deepLinkUri)
        navController.navigate(uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhotoAuthorizationFragment()
    }
}
