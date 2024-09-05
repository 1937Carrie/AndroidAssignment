package com.dumchykov.socialnetworkdemo.data

import android.util.Patterns

fun validateEmail(email: CharSequence): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password: CharSequence): Boolean {
    return password.length >= 6
}