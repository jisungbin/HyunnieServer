package com.sungbin.hyunnieserver.ui.activity

import android.Manifest
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.sungbin.androidutils.util.PermissionUtil
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.databinding.ActivityMainBinding
import com.sungbin.hyunnieserver.model.File
import com.sungbin.hyunnieserver.tool.ui.NotificationUtil
import org.apache.commons.net.ftp.FTPClient


/**
 * Created by SungBin on 2020-08-23.
 */

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    companion object { // 이게 맞나;;;
        val client = FTPClient().apply { controlEncoding = "UTF-8" }
        val config = Firebase.remoteConfig
        val fileList: MutableLiveData<List<File>> = MutableLiveData()
        val fileCache = HashMap<String, List<File>>()
        lateinit var backPressedAction: () -> Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_container) as NavHostFragment
        navController = navHostFragment.navController

        if (!PermissionUtil.checkPermissionsAllGrant(
                applicationContext,
                arrayOf(
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        ) {
            PermissionUtil.request(
                this,
                getString(R.string.main_need_permission),
                arrayOf(
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
            NotificationUtil.createChannel(applicationContext)
        }

        supportActionBar?.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_navigation, menu)
        binding.sbbNavigation.setupWithNavController(menu!!, navController)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }

    override fun onBackPressed() {
        backPressedAction()
    }

}