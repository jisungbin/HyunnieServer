package com.sungbin.hyunnieserver.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.ui.dialog.LoadingDialog
import com.sungbin.sungbintool.util.NetworkUtil
import org.jetbrains.anko.startActivity


/**
 * Created by SungBin on 2020-08-23.
 */

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_splash)

        if (NetworkUtil.isNetworkAvailable(applicationContext)) {
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
                if (DataUtil.readData(
                        applicationContext,
                        PathManager.SERVER_ADDRESS,
                        "null"
                    ) == "null"
                ) {
                    startActivity<JoinActivity>()
                } else {
                    startActivity<MainActivity>()
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }, 1500)
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
