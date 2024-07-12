package com.example.finalproject.domain.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.PaymentData

interface GoogleRepository {
//    suspend fun verifyGooglePayReadiness()
    suspend fun fetchCanUseGooglePay(): Boolean
    suspend fun getLoadPaymentDataTask(priceCents: Long): Task<PaymentData>
//    suspend fun handleError(statusCode: Int, message: String?)
//    suspend fun setPaymentData(paymentData: PaymentData)
    suspend fun extractPaymentBillingName(paymentData: PaymentData): String?
}