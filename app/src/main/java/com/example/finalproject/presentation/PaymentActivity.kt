package com.example.finalproject.presentation

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.startActivity
import com.example.finalproject.presentation.checkout.CheckoutScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CheckoutScreen(
                modifier = Modifier,
                onContinueShoppingClick = { startMainActivity() }
            )
        }
    }
}

private fun startMainActivity() {
}