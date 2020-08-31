package com.sungbin.hyunnieserver.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.ui.dialog.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_join.*
import retrofit2.Retrofit
import javax.inject.Inject


/**
 * Created by SungBin on 2020-08-23.
 */

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {

    private val CODE_REQUEST_STORAGE_ACCESS = 3000

    @Inject
    lateinit var client: Retrofit

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_join)

        btn_request_storage.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                CODE_REQUEST_STORAGE_ACCESS
            )
        } 
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            btn_request_storage.apply {
                text = getString(R.string.join_permission_grant)
                setOnClickListener { }
                alpha = 0.5f

            }

            btn_start_with_login.apply {
                alpha = 1f
                setOnClickListener {
                    checkLogin(
                        tiet_server_address.text.toString(),
                        tiet_id.text.toString(),
                        tiet_password.text.toString()
                    )
                }
            }
        }
    }

    private fun checkLogin(address: String, id: String, pw: String): Boolean {
        // todo: 로그인 체크 기능
        return true
    }

}