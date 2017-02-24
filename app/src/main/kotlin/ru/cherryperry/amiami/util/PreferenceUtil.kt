package ru.cherryperry.amiami.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.util.*
import kotlin.reflect.KProperty

class StringPreference(context: Context, keyId: Int, default: String) {
    private val key: String = context.getString(keyId)
    private val defaultValue: String = default
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = preferences.getString(key, defaultValue)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) =
            preferences.edit().putString(key, value).apply()
}

class BooleanPreference(context: Context, keyId: Int, default: Boolean) {
    private val key: String = context.getString(keyId)
    private val defaultValue: Boolean = default
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = preferences.getBoolean(key, defaultValue)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) =
            preferences.edit().putBoolean(key, value).apply()
}

class IntPreference(context: Context, keyId: Int, default: Int) {
    private val key: String = context.getString(keyId)
    private val defaultValue: Int = default
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int = preferences.getInt(key, defaultValue)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) =
            preferences.edit().putInt(key, value).apply()
}

class StringSetPreference(context: Context, keyId: Int) {
    private val key: String = context.getString(keyId)
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): MutableSet<String> = preferences.getStringSet(key, HashSet())

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: MutableSet<String>) =
            preferences.edit().putStringSet(key, value).apply()
}