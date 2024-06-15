package tech.reliab.todoapp.domain.model

import android.graphics.Color

enum class CategoryEnum(val text: String, val color: Int) {
    Work("Работа", Color.parseColor("#00BFFF")),       // Deep Sky Blue
    Cleaning("Уборка", Color.parseColor("#3CB371")),   // Medium Sea Green
    Cooking("Готовка", Color.parseColor("#FF7F50")),   // Coral
    Studies("Учеба", Color.parseColor("#DAA520")),     // Goldenrod
    Other("Своя категория", Color.parseColor("#6A5ACD")) // Slate Blue
}

fun getCategoryEnumByText(text: String): CategoryEnum {
    return CategoryEnum.entries.find { it.text == text } ?: CategoryEnum.Other
}