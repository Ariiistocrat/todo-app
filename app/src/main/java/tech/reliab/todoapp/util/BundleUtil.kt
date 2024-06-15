package tech.reliab.todoapp.util

import android.os.Build
import android.os.Bundle
import java.io.Serializable


fun Bundle.getStringIfContains(key: String, defaultValue: String? = null): String? {
    return if (containsKey(key))
        getString(key)
    else
        defaultValue
}

fun Bundle.getIntIfContains(key: String, defaultValue: Int? = null): Int? {
    return if (containsKey(key))
        getInt(key)
    else
        defaultValue
}

fun Bundle.getBooleanIfContains(key: String, defaultValue: Boolean? = null): Boolean? {
    return if (containsKey(key))
        getBoolean(key)
    else
        defaultValue
}

inline fun <reified T : Serializable> Bundle.getSerializableIfContains(key: String): T? {
    return if (containsKey(key)) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(key, T::class.java)
            } else {
                getSerializable(key) as? T
            }
        } catch (e: ClassCastException) {
            null
        }
    } else {
        null
    }
}