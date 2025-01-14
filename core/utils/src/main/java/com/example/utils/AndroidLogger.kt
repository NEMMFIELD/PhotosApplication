package com.example.utils

import android.util.Log

class AndroidLogger:Logger {
    override fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }

    override fun d(tag: String, message: String) {
        Log.d(tag,message)
    }

}
interface Logger {
    fun e(tag: String, message: String, throwable: Throwable)
    fun d(tag: String, message: String)
}
