package com.vortex.mail_man_1.viewmodel

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser?) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
} 