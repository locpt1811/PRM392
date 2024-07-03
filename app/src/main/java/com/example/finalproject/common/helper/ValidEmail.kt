package com.example.finalproject.common.helper

import android.util.Patterns.EMAIL_ADDRESS

object ValidEmail {

    fun verifyEmailType(email: String): Boolean = EMAIL_ADDRESS.matcher(email).matches()
}