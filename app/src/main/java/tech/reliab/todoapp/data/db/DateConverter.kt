package tech.reliab.todoapp.data.db

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromTimeStamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimeStamp(date: Date): Long = date.time
}