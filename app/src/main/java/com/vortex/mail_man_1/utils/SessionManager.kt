package com.vortex.mail_man_1.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseUser

/**
 * Manages user session data and preferences
 */
class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    /**
     * Saves user session data
     */
    fun saveUserSession(user: FirebaseUser?) {
        editor.apply {
            putString(KEY_USER_ID, user?.uid)
            putString(KEY_EMAIL, user?.email)
            putString(KEY_DISPLAY_NAME, user?.displayName)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    /**
     * Clears user session data
     */
    fun clearSession() {
        editor.apply {
            clear()
            apply()
        }
    }

    /**
     * Checks if user is logged in
     */
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    /**
     * Gets current user ID
     */
    fun getCurrentUserId(): String? = prefs.getString(KEY_USER_ID, null)

    companion object {
        private const val PREF_NAME = "MailManPrefs"
        private const val KEY_USER_ID = "userId"
        private const val KEY_EMAIL = "email"
        private const val KEY_DISPLAY_NAME = "displayName"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }
} 