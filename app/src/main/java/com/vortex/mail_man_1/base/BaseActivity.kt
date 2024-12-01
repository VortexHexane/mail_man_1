package com.vortex.mail_man_1.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.vortex.mail_man_1.utils.SessionManager

/**
 * Base activity class with common functionality
 */
abstract class BaseActivity : ComponentActivity() {
    protected lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
        checkSession()
    }

    /**
     * Checks if user session is valid
     */
    protected open fun checkSession() {
        if (!sessionManager.isLoggedIn()) {
            // Navigate to login screen if needed
            navigateToLogin()
        }
    }

    /**
     * Navigate to login screen
     */
    abstract fun navigateToLogin()
} 