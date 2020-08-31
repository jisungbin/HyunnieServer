package com.sungbin.hyunnieserver

import android.app.Application
import android.content.Context
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp


/**
 * Created by SungBin on 2020-08-31.
 */

@HiltAndroidApp
class HyunnieServer : Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        context = applicationContext
    }

}