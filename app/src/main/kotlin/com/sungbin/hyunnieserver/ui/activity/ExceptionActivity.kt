package com.sungbin.hyunnieserver.ui.activity

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.sungbin.hyunnieserver.R
import kotlinx.android.synthetic.main.activity_exception.*


/**
 * Created by SungBin on 2020-08-31.
 */

class ExceptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exception)

        val message = intent.getStringExtra("message") ?: "NullPointerException"
        tv_except.apply {
            val ssb = SpannableStringBuilder(message)
            ssb.setSpan(
                StyleSpan(Typeface.ITALIC),
                message.lastIndexOf("#"),
                message.lastIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = ssb
            movementMethod = ScrollingMovementMethod()
        }

        lav_exception.setOnClickListener {
            lav_exception.playAnimation()
        }
    }

}