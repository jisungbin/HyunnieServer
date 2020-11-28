package com.sungbin.hyunnieserver

import android.app.Application
import android.os.StrictMode
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.sungbin.hyunnieserver.tool.util.ExceptionUtil


/**
 * Created by SungBin on 2020-08-31.
 */

class HyunnieServer : Application() {

    override fun onCreate() {
        super.onCreate()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build() // 이거 하면 안되는데...
        StrictMode.setThreadPolicy(policy)
        Firebase.remoteConfig.fetchAndActivate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            ExceptionUtil.except(Exception(throwable), applicationContext)
        }
    }

}