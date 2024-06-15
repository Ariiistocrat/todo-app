package tech.reliab.todoapp.util

import android.graphics.Color
import kotlin.math.abs

fun getRandomColor(): Int {
    val red = abs((0..255).random())
    val green = abs((0..255).random())
    val blue = abs((0..255).random())
    return Color.rgb(red, green, blue)
}