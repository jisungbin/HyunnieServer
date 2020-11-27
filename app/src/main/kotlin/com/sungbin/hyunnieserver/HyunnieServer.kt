package com.sungbin.hyunnieserver

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.sungbin.hyunnieserver.tool.ui.NotificationUtil
import com.sungbin.hyunnieserver.tool.util.ExceptionUtil
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

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            ExceptionUtil.except(Exception(throwable), applicationContext)
        }

        Firebase.remoteConfig.fetchAndActivate()

        NotificationUtil.createChannel(applicationContext)
    }

}