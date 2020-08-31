package com.sungbin.hyunnieserver.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.sungbin.hyunnieserver.R
import com.sungbin.sungbintool.extensions.get
import com.sungbin.sungbintool.extensions.plusAssign


class LoadingDialog(private val activity: Activity) {

    private lateinit var alert: AlertDialog

    @SuppressLint("InflateParams")
    private var layout: View =
        LayoutInflater.from(activity).inflate(R.layout.layout_loading_dialog, null)

    init {
        val dialog = AlertDialog.Builder(activity)
        dialog.setView(layout)

        alert = dialog.create()
        alert.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    layout.context,
                    android.R.color.transparent
                )
            )
        )
        alert.setCancelable(false)
    }

    fun show() {
        alert.show()
    }

    fun updateTitle(title: String) {
        (layout[R.id.tv_loading] as TextView) += title
        layout.invalidate()
    }

    fun setCustomState(
        lottie: Int,
        message: String,
        canDismiss: Boolean,
        dismissListener: () -> Unit
    ) {
        (layout[R.id.lav_load] as LottieAnimationView).run {
            setAnimation(lottie)
            playAnimation()
        }
        (layout[R.id.tv_loading] as TextView) += message
        (layout[R.id.tv_loading] as TextView).movementMethod = ScrollingMovementMethod()
        alert.setCancelable(canDismiss)
        alert.setOnDismissListener { dismissListener() }
        layout.invalidate()
    }

    fun setError(exception: Exception) {
        (layout[R.id.lav_load] as LottieAnimationView).run {
            setAnimation(R.raw.error)
            playAnimation()
        }
        (layout[R.id.tv_loading] as TextView).run {
            val message =
                "서버 요청 중 오류가 발생했습니다!\n\n${exception.message} #${exception.stackTrace[0].lineNumber}"
            val ssb = SpannableStringBuilder(message)
            ssb.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorLightRed)),
                message.lastIndexOf("\n"),
                message.lastIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = ssb
        }
        (layout[R.id.tv_loading] as TextView).movementMethod = ScrollingMovementMethod()
        alert.setCancelable(true)
        layout.invalidate()
    }

    fun close() {
        alert.cancel()
    }
}