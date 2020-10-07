package com.sungbin.hyunnieserver.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.tool.util.ExceptionUtil
import com.sungbin.sungbintool.util.DataUtil
import com.sungbin.sungbintool.util.ToastLength
import com.sungbin.sungbintool.util.ToastType
import com.sungbin.sungbintool.util.ToastUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_join.*
import org.apache.commons.net.ftp.FTPClient
import org.jetbrains.anko.startActivity
import javax.inject.Inject


/**
 * Created by SungBin on 2020-08-23.
 */

@AndroidEntryPoint
class JoinActivity : AppCompatActivity() {

    private val CODE_REQUEST_STORAGE_ACCESS = 3000

    @Inject
    lateinit var client: FTPClient

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
                    val address = tiet_server_address.text.toString()
                    val id = tiet_id.text.toString()
                    val password = tiet_password.text.toString()
                    if (login(address, id, password)) {
                        ToastUtil.show(
                            context,
                            context.getString(R.string.join_welcome),
                            ToastLength.SHORT,
                            ToastType.SUCCESS
                        )
                        DataUtil.saveData(context, PathManager.SERVER_ADDRESS, address)
                        DataUtil.saveData(context, PathManager.ID, id)
                        DataUtil.saveData(context, PathManager.PASSWORD, password)
                        startActivity<MainActivity>()
                    } else {
                        ToastUtil.show(
                            context,
                            context.getString(R.string.join_wrong_data),
                            ToastLength.SHORT,
                            ToastType.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun login(address: String, id: String, password: String): Boolean {
        return try {
            client.connect(address)
            client.login(id, password)
        } catch (exception: Exception) {
            ExceptionUtil.except(exception, applicationContext)
            false
        }
    }

}