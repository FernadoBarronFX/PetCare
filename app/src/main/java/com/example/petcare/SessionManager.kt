package com.example.petcare

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "PetCareSession"
    private const val KEY_USER_ID = "userId"

    fun guardarSesion(context: Context, userId: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun obtenerSesion(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun cerrarSesion(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

}
