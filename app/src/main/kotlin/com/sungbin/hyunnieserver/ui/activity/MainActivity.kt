package com.sungbin.hyunnieserver.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commitNow
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.ui.fragment.main.MainFragment
import com.sungbin.hyunnieserver.ui.tool.manager.PathManager
import com.sungbin.sungbintool.DataUtils
import com.sungbin.sungbintool.StorageUtils


/**
 * Created by SungBin on 2020-08-23.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        StorageUtils.createFolder(
            DataUtils.readData(
                applicationContext,
                PathManager.DOWNLOAD_PATH,
                "HyunnieServer/Download"
            ), true
        )

        supportFragmentManager.commitNow {
            add(R.id.fl_container, MainFragment.instance())
        }
    }

    /*override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.fl_container)
        (fragment as? OnBackPressedUtil)?.onBackPressed(this)
    }*/

}