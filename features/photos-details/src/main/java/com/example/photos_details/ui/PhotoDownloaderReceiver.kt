package com.example.photos_details.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ScrollView

import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

class PhotoDownloadReceiver(private val rootView: View) : BroadcastReceiver() {
    private var rootViewReference: WeakReference<ScrollView>? = null
    fun setRootView(rootView: ScrollView) {
        rootViewReference = WeakReference(rootView)
    }

    override fun onReceive(p0: Context, intent: Intent?) {
        rootViewReference?.get()?.let { rootView ->
            if (intent?.action == "com.example.LOCAL_DOWNLOAD_COMPLETE") {
                val message = intent.getStringExtra("MESSAGE") ?: "Download complete"
                Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    fun clearRootView() {
        rootViewReference = null
    }
}
