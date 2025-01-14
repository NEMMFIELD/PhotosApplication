package com.example.photosapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import com.example.photos_random.ui.RandomPhotoFragment
import com.example.photosapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1
    private lateinit var binding: ActivityMainBinding
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //   val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        //  val navController = navHostFragment.navController
        setupBottomNavigationView()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение предоставлено
                Log.d("MainActivity", "POST_NOTIFICATIONS permission granted.")
            } else {
                // Разрешение отклонено
                Log.d("MainActivity", "POST_NOTIFICATIONS permission denied.")
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            if (it.getStringExtra("ACTION") == "REFRESH_PHOTOS") {
                refreshPhotos()
            }
        }
    }

    private fun setupBottomNavigationView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = binding.bottomNavigation
        navHostFragment.navController.let { bottomNavigationView?.setupWithNavController(it) }
    }

    private fun refreshPhotos() {
        val navController = findNavController(R.id.nav_host_fragment)
        val currentDestination = navController.currentDestination?.id

        if (currentDestination == R.id.randomPhotoFragment) {
            // Если фрагмент списка открыт, обновляем данные
            val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            (fragment as? RandomPhotoFragment)?.refreshData()
        } else {
            // Если список закрыт, перенаправляем на него
            navController.navigate(R.id.randomPhotoFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        WorkManager.getInstance(this).cancelUniqueWork("RandomWorker")
    }
}





