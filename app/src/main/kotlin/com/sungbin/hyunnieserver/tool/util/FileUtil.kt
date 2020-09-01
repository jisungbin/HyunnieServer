package com.sungbin.hyunnieserver.tool.util

import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.tool.manager.TypeManager
import com.sungbin.sungbintool.extensions.replaceLast
import java.util.*

object FileUtil {

    fun getTypeIcon(type: Int): Int {
        return when (type) {
            TypeManager.FOLDER -> R.drawable.ic_baseline_folder_24
            TypeManager.IMAGE -> R.drawable.ic_baseline_image_24
            TypeManager.TEXT -> R.drawable.ic_baseline_book_24
            TypeManager.VIDEO -> R.drawable.ic_baseline_ondemand_video_24
            TypeManager.GIF -> R.drawable.ic_baseline_gif_24
            TypeManager.CODE -> R.drawable.ic_baseline_code_24
            TypeManager.APK -> R.drawable.ic_baseline_android_24
            TypeManager.ZIP -> R.drawable.ic_baseline_work_outline_24
            TypeManager.EXE -> R.drawable.ic_baseline_desktop_windows_24
            TypeManager.MUSIC -> R.drawable.ic_baseline_music_note_24
            TypeManager.BOOK -> R.drawable.ic_baseline_menu_book_24
            TypeManager.PDF -> R.drawable.ic_baseline_picture_as_pdf_24
            TypeManager.FILE -> R.drawable.ic_baseline_insert_drive_file_24
            TypeManager.SUBTITLE -> R.drawable.ic_baseline_translate_24
            else -> R.drawable.ic_baseline_folder_24
        }
    }

    fun getType(name: String, size: Long): Int {
        return if (name.contains(".")) {
            with(name) {
                when {
                    endsWith("png", true) -> TypeManager.IMAGE
                    endsWith("jpg", true) -> TypeManager.IMAGE
                    endsWith(
                        "txt",
                        true
                    ) -> if (size >= 10000) TypeManager.BOOK else TypeManager.TEXT
                    endsWith(
                        "text",
                        true
                    ) -> if (size >= 10000) TypeManager.BOOK else TypeManager.TEXT
                    endsWith("mp4", true) -> TypeManager.VIDEO
                    endsWith("avi", true) -> TypeManager.VIDEO
                    endsWith("gif", true) -> TypeManager.GIF
                    endsWith("js", true) -> TypeManager.CODE
                    endsWith("APK", true) -> TypeManager.APK
                    endsWith("zip", true) -> TypeManager.ZIP
                    endsWith("egg", true) -> TypeManager.ZIP
                    endsWith("exe", true) -> TypeManager.EXE
                    endsWith("mp3", true) -> TypeManager.MUSIC
                    endsWith("pdf", true) -> TypeManager.PDF
                    endsWith("smi", true) -> TypeManager.SUBTITLE
                    else -> TypeManager.FILE
                }
            }
        } else {
            TypeManager.FOLDER
        }
    }

    fun getLastModifyTime(calendar: Calendar): String {
        val timeData = calendar.time.toString().split(" ")
        val month = timeData[1].en2num()
        val day = timeData[2]
        var time = timeData[3].replaceLast(":00", "")
        val year = timeData[5]
        time = if (time == "00:00") ""
        else " $time"
        return "$year.$month.$day$time" // 2020.12.02 07:22:26
    }

    private fun String.en2num() =
        this.replace("Jan", "01").replace("Feb", "02").replace("Mar", "03").replace("Apr", "04")
            .replace("May", "05").replace("Jun", "06").replace("Jul", "07").replace("Aug", "08")
            .replace("Sep", "09").replace("Oct", "10").replace("Nov", "11").replace("Dec", "12")
}