package com.sungbin.hyunnieserver.tool.util

import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.model.FileType
import com.sungbin.sungbintool.extensions.replaceLast
import java.util.*

object FileUtil {

    fun getTypeIcon(fileType: FileType): Int {
        return when (fileType) {
            FileType.FOLDER -> R.drawable.ic_baseline_folder_24
            FileType.IMAGE -> R.drawable.ic_baseline_image_24
            FileType.TEXT -> R.drawable.ic_baseline_book_24
            FileType.VIDEO -> R.drawable.ic_baseline_ondemand_video_24
            FileType.GIF -> R.drawable.ic_baseline_gif_24
            FileType.CODE -> R.drawable.ic_baseline_code_24
            FileType.APK -> R.drawable.ic_baseline_android_24
            FileType.ZIP -> R.drawable.ic_baseline_work_outline_24
            FileType.EXE -> R.drawable.ic_baseline_desktop_windows_24
            FileType.MUSIC -> R.drawable.ic_baseline_music_note_24
            FileType.BOOK -> R.drawable.ic_baseline_menu_book_24
            FileType.PDF -> R.drawable.ic_baseline_picture_as_pdf_24
            FileType.FILE -> R.drawable.ic_baseline_insert_drive_file_24
            FileType.SUBTITLE -> R.drawable.ic_baseline_translate_24
            else -> R.drawable.ic_baseline_folder_24
        }
    }

    fun getType(name: String, size: Long): FileType {
        return if (name.contains(".")) {
            with(name) {
                when {
                    endsWith("png", true) -> FileType.IMAGE
                    endsWith("jpg", true) -> FileType.IMAGE
                    endsWith(
                        "txt",
                        true
                    ) -> if (size >= 10000) FileType.BOOK else FileType.TEXT
                    endsWith(
                        "text",
                        true
                    ) -> if (size >= 10000) FileType.BOOK else FileType.TEXT
                    endsWith("mp4", true) -> FileType.VIDEO
                    endsWith("avi", true) -> FileType.VIDEO
                    endsWith("gif", true) -> FileType.GIF
                    endsWith("js", true) -> FileType.CODE
                    endsWith("APK", true) -> FileType.APK
                    endsWith("zip", true) -> FileType.ZIP
                    endsWith("egg", true) -> FileType.ZIP
                    endsWith("exe", true) -> FileType.EXE
                    endsWith("mp3", true) -> FileType.MUSIC
                    endsWith("pdf", true) -> FileType.PDF
                    endsWith("smi", true) -> FileType.SUBTITLE
                    else -> FileType.FILE
                }
            }
        } else {
            FileType.FOLDER
        }
    }

    fun getLastModifyTime(calendar: Calendar): String {
        val timeData = calendar.time.toString().split(" ")
        val month = timeData[1].eng2num()
        val day = timeData[2]
        var time = timeData[3].replaceLast(":00", "")
        val year = timeData[5]
        time = if (time == "00:00") "" else " $time"
        return "$year.$month.$day $time" // 2020.12.02 07:22:26
    }

    private fun String.eng2num() =
        this.replace("Jan", "01").replace("Feb", "02").replace("Mar", "03").replace("Apr", "04")
            .replace("May", "05").replace("Jun", "06").replace("Jul", "07").replace("Aug", "08")
            .replace("Sep", "09").replace("Oct", "10").replace("Nov", "11").replace("Dec", "12")
}