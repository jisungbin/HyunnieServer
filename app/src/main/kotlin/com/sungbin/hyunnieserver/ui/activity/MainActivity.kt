package com.sungbin.hyunnieserver.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.tool.ui.NotificationUtil
import com.sungbin.hyunnieserver.tool.util.OnBackPressedUtil
import com.sungbin.hyunnieserver.ui.fragment.main.MainFragment
import com.sungbin.sungbintool.util.DataUtil
import com.sungbin.sungbintool.util.StorageUtil
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by SungBin on 2020-08-23.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NotificationUtil.createChannel(applicationContext)
        val notificationId = 1000
        val manager = NotificationUtil.getManager(applicationContext)
        val notification = NotificationUtil.getDownloadNotification(
            applicationContext, notificationId, getString(
                R.string.notification_downloading
            ), "test notification"
        )
        manager.notify(notificationId, notification.build())

        DataUtil.readData(
            applicationContext,
            PathManager.DOWNLOAD_PATH,
            PathManager.DOWNLOAD_PATH_DEFAULT
        )?.let {
            StorageUtil.createFolder(
                it, true
            )
        }

        supportFragmentManager.commitNow {
            add(R.id.fl_container, MainFragment.instance())
        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.fl_container)
        (fragment as? OnBackPressedUtil)?.onBackPressed(this)
    }

}