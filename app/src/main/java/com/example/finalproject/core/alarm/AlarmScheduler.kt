package com.example.finalproject.core.alarm

import android.app.PendingIntent

interface AlarmScheduler {
    fun createPendingIntent(): PendingIntent
    fun schedule()
}