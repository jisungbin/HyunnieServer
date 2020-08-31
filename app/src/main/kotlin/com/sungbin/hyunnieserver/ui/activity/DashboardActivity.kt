package com.sungbin.hyunnieserver.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sungbin.hyunnieserver.R


/**
 * Created by SungBin on 2020-08-23.
 */

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        /*StorageUtils.createFolder(PathManager.JS, true)
        StorageUtils.createFolder(PathManager.SIMPLE, true)
        StorageUtils.createFolder(PathManager.DATABASE, true)
        StorageUtils.createFolder(PathManager.LOG, true)
        StorageUtils.createFolder(PathManager.SENDER, true)
        StorageUtils.createFolder(PathManager.ROOM, true)

        supportFragmentManager.commitNow {
            add(R.id.fl_container, BotFragment.instance())
        }*/
    }

    /*override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.fl_container)
        (fragment as? OnBackPressedUtil)?.onBackPressed(this)
    }*/

}