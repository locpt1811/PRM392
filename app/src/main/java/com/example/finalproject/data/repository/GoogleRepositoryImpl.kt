package com.example.finalproject.data.repository

import android.util.Log
import com.example.finalproject.domain.repository.GoogleRepository
import com.example.finalproject.presentation.checkout.PaymentUiState
import com.example.finalproject.utils.PaymentsUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import kotlinx.coroutines.tasks.await
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class GoogleRepositoryImpl @Inject constructor (

//    private val ioDispatcher: CoroutineDispatcher,
    private val paymentsClient: PaymentsClient,
) : GoogleRepository {

    /**
     * Determine the user's ability to pay with a payment method supported by your app and display
     * a Google Pay payment button.
    ) */
//    override suspend fun verifyGooglePayReadiness() {
//        val newUiState: PaymentUiState = try {
//            if (fetchCanUseGooglePay()) {
//                PaymentUiState.Available
//            } else {
//                PaymentUiState.Error(CommonStatusCodes.ERROR)
//            }
//        } catch (exception: ApiException) {
//            PaymentUiState.Error(exception.statusCode, exception.message)
//        }
//
//        _paymentUiState.update { newUiState }
//    }

    /**
     * Determine the user's ability to pay with a payment method supported by your app.
    ) */
    override suspend fun fetchCanUseGooglePay(): Boolean {
        val request = IsReadyToPayRequest.fromJson(PaymentsUtil.isReadyToPayRequest().toString())
        return paymentsClient.isReadyToPay(request).await()
    }

    /**
     * Creates a [Task] that starts the payment process with the transaction details included.
     *
     * @return a [Task] with the payment information.
     * @see [PaymentDataRequest](https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient#loadPaymentData(com.google.android.gms.wallet.PaymentDataRequest)
    ) */
    override suspend fun getLoadPaymentDataTask(priceCents: Long): Task<PaymentData> {
        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        return paymentsClient.loadPaymentData(request)
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     * WalletConstants.ERROR_CODE_* constants.
     * @see [
     * Wallet Constants Library](https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.constant-summary)
     */
//    override suspend fun handleError(statusCode: Int, message: String?) {
//        Log.e("Google Pay API error", "Error code: $statusCode, Message: $message")
//    }

//    override suspend fun setPaymentData(paymentData: PaymentData) {
//        val payState = extractPaymentBillingName(paymentData)?.let {
//            PaymentUiState.PaymentCompleted(payerName = it)
//        } ?: PaymentUiState.Error(CommonStatusCodes.INTERNAL_ERROR)
//
//        _paymentUiState.update { payState }
//    }

    override suspend fun extractPaymentBillingName(paymentData: PaymentData): String? {
        val paymentInformation = paymentData.toJson()

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData =
                JSONObject(paymentInformation).getJSONObject("paymentMethodData")
            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            // Logging token string.
            Log.d(
                "Google Pay token", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
            )

            return billingName
        } catch (error: JSONException) {
            Log.e("handlePaymentSuccess", "Error: $error")
        }

        return null
    }

}