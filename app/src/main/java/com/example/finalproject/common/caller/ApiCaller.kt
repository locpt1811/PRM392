package com.example.finalproject.common.caller

import android.util.Log
import com.example.finalproject.R
import com.example.finalproject.common.Response
import java.io.IOException

suspend inline fun <T> apiCall(crossinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: IOException) {
        Response.Error(errorMessageId = R.string.network)
    } catch (e: Exception) {
        Log.d("API CALL EXCEPTION", e.message.toString())
        Response.Error(errorMessageId = R.string.unknown_error)
    }
}