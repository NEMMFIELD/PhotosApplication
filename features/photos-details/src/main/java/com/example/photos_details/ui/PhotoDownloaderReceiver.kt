package com.example.photos_details.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View

import com.google.android.material.snackbar.Snackbar

class PhotoDownloadReceiver(private val rootView: View):BroadcastReceiver() {
    override fun onReceive(p0: Context, intent: Intent?) {
        if (intent?.action == "com.example.LOCAL_DOWNLOAD_COMPLETE") {
            val message = intent.getStringExtra("MESSAGE") ?: "Download complete"
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}
