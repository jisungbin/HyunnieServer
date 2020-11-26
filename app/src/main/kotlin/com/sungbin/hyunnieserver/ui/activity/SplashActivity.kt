package com.sungbin.hyunnieserver.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sungbin.androidutils.extensions.doDelay
import com.sungbin.androidutils.util.NetworkUtil
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.ui.dialog.LoadingDialog
import org.jetbrains.anko.startActivity


/**
 * Created by SungBin on 2020-08-23.
 */

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_splash)

        if (NetworkUtil.isNetworkAvailable(applicationContext)) {
            doDelay(1500) {
                finish()
                startActivity<MainActivity>()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        } else {
            LoadingDialog(this).apply {
                setCustomState(
                    R.raw.no_internet,
                    getString(R.string.join_no_connect_internet),
                    true
                ) {
                    finish()
                }
            }.show()
        }
    }

    override fun onBackPressed() {}
}
