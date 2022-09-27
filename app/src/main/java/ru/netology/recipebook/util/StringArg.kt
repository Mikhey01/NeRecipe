package ru.netology.recipebook.util

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringArg {

    object StringArg : ReadWriteProperty<Bundle, String?> {

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }

        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
            thisRef.getString(property.name)
    }


    object LongArg : ReadWriteProperty<Bundle, Long> {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): Long {
            return thisRef.getLong(property.name)
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Long) {
            thisRef.putLong(property.name, value)
        }
    }
}