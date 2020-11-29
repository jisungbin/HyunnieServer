package com.sungbin.hyunnieserver.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.sungbin.androidutils.extensions.plusAssign
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.databinding.LayoutLoadingDialogBinding


class LoadingDialog(activity: Activity) {

    private var alert: AlertDialog
    private var layout = LayoutLoadingDialogBinding.inflate(LayoutInflater.from(activity))

    init {
        val dialog = AlertDialog.Builder(activity)
        dialog.setView(layout.root)

        alert = dialog.create()
        alert.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    layout.root.context,
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
        layout.tvLoading += title
        layout.root.invalidate()
    }

    fun setCustomState(
        lottie: Int,
        message: String,
        canDismiss: Boolean,
        dismissListener: () -> Unit
    ) {
        layout.run {
            lavLoad.setAnimation(lottie)
            lavLoad.playAnimation()
            tvLoading += message
            tvLoading.movementMethod = ScrollingMovementMethod()
        }
        alert.setCancelable(canDismiss)
        alert.setOnDismissListener { dismissListener() }
        layout.root.invalidate()
    }

    fun setError(exception: Exception) {
        layout.run { // 이게 맞나;;
            lavLoad.run {
                setAnimation(R.raw.error)
                playAnimation()
            }
            tvLoading.run {
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
                movementMethod = ScrollingMovementMethod()
            }
        }
        alert.setCancelable(true)
        layout.root.invalidate()
    }

    fun close() {
        alert.cancel()
    }
}