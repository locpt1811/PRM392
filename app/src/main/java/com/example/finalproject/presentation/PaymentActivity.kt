package com.example.finalproject.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.finalproject.presentation.cart.CartViewModel
import com.example.finalproject.presentation.checkout.CheckoutScreen
import com.example.finalproject.presentation.checkout.CheckoutViewModel
import com.example.finalproject.presentation.checkout.PaymentUiState
import com.example.finalproject.presentation.navigation.MainDestinations
import com.example.finalproject.utils.FIRST_TIME_LAUNCH
import com.example.finalproject.utils.PaymentsUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.contract.TaskResultContracts


class PaymentActivity : ComponentActivity() {

    private val paymentDataLauncher = registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    Log.i("Google Pay result:", it.toJson())
                }
            }
            //CommonStatusCodes.CANCELED -> The user canceled
            //AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
            //CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
        }
    }
    private val model: CheckoutViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView(R.layout.activity_payment) // Uncomment this if you have a layout for this activity
        Log.d("PaymentActivity", "already here")
        // Here you can initiate the payment process

    }



    fun requestPayment() {

        Log.d("PaymentActivity", "request here")
        val task = model.getLoadPaymentDataTask(priceCents = 1000L)
        task.addOnCompleteListener(paymentDataLauncher::launch)
    }


}