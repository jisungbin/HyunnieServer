package com.sungbin.hyunnieserver.ui.activity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.tool.util.OnBackPressedUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by SungBin on 2020-08-23.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_container) as NavHostFragment
        navController = navHostFragment.navController

        supportActionBar?.hide()

        /* NotificationUtil.createChannel(applicationContext)
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
         }*/
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_navigation, menu)
        sbb_navigation.setupWithNavController(menu!!, navController)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.fcv_container)
        (fragment as? OnBackPressedUtil)?.onBackPressed(this)
    }

}