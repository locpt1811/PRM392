package com.example.finalproject.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.finalproject.core.notification.ShoppingNotifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var shoppingNotifier: ShoppingNotifier

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            shoppingNotifier.launchNotification()
        }
    }
}