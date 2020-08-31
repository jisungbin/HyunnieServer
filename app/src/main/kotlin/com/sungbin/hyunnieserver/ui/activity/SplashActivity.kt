package com.sungbin.hyunnieserver.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.ui.tool.manager.PathManager
import com.sungbin.sungbintool.DataUtils
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

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
            if (DataUtils.readData(applicationContext, PathManager.SERVER_ADDRESS, "null") == "null") {
                startActivity<JoinActivity>()
            } else {
                startActivity<DashboardActivity>()
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 1500)
    }

    override fun onBackPressed() {}
}
