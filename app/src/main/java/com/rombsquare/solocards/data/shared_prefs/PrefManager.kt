package com.rombsquare.solocards.data.shared_prefs

import android.content.Context
import androidx.core.content.edit

class PrefManager(context: Context) {
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun getShowAnswer(): Boolean = prefs.getBoolean("show_answer", false)
    fun setShowAnswer(state: Boolean) = prefs.edit { putBoolean("show_answer", state) }

    fun getFirstRun(): Boolean = prefs.getBoolean("first_run", true)
    fun setFirstRunFalse() = prefs.edit { putBoolean("first_run", false) }
}