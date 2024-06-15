package tech.reliab.todoapp.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class DateToString {
    @SuppressLint("SimpleDateFormat")
    companion object {
        fun convertDateToString(date: Date): String {
            val format1 = "d MMM yyyy"
            val format2 = "d MMM yyyy HH:mm"
            val dateInfinity = Date(Constants.MAX_TIMESTAMP)
            return if (dateInfinity.compareTo(date) == 0) "N/A"
            else if (date.seconds == 0) {
                val df = SimpleDateFormat(format1)
                df.format(date)
            } else {
                val df = SimpleDateFormat(format2)
                df.format(date)
            }
        }
    }
}