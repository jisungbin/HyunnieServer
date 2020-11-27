package com.sungbin.hyunnieserver.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by SungBin on 2020-08-23.
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_container) as NavHostFragment
        navController = navHostFragment.navController

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
        AlertDialog.Builder(this).run {
            setTitle(getString(R.string.close))
            setMessage(getString(R.string.main_really_close))
            setNeutralButton(getString(R.string.main_stay)) { _, _ -> }
            setPositiveButton(getString(R.string.main_finish)) { _, _ ->
                finish()
            }
        }.show()
    }

}