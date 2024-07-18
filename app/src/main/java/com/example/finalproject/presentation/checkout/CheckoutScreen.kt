package com.example.finalproject.presentation.checkout

import com.example.finalproject.presentation.PaymentActivity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalproject.R
import com.example.finalproject.presentation.designsystem.components.ShoppingButton
import com.example.finalproject.utils.PaymentsUtil
import com.google.pay.button.PayButton
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.finalproject.presentation.navigation.MainDestinations

@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    onContinueShoppingClick: () -> Unit,
    onGooglePayButtonClick: (Float) -> Unit,
    payUiState: PaymentUiState = PaymentUiState.PaymentCompleted(""),
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val padding = 20.dp
    val black = Color(0xff000000.toInt())
    val grey = Color(0xffeeeeee.toInt())
    val context = LocalContext.current
    val paymentAmount = 1200f
    val uiState by viewModel.paymentUiState.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    Log.d("CheckoutScreen", "Recomposing with isSuccess: $isSuccess")
    LaunchedEffect(isSuccess) {
        Log.d("CheckoutScreen", "Recomposing with isSuccess: $isSuccess")
        Log.d("CheckoutScreen", "Recomposing with paysate: $payUiState")
    }
    if ( payUiState is PaymentUiState.PaymentCompleted) {
        Column(
            modifier = Modifier
                .testTag("successScreen")
                .background(grey)
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                contentDescription = null,
                painter = painterResource(R.drawable.check_circle),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
//                ${payUiState.payerName}
                text = "You has completed a payment.\nWe are preparing your order.",
                fontSize = 17.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            ShoppingButton(
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.two_level_margin)),
                onClick = onContinueShoppingClick,
                buttonText = stringResource(id = R.string.continue_shopping)
            )
        }

    }
    else {
        Text(text = "checkout screen")

        Column(
            modifier = Modifier
                .background(grey)
                .padding(padding)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(space = padding / 2),
        ) {
            Text(
                text = "Total money: ${(viewModel.totalAmount).toFloat()}",
                fontSize = 24.sp, // Adjust the font size as needed
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = padding)
            )


//            if (payUiState !is PaymentUiState.NotStarted) {
                PayButton(
                    modifier = Modifier
                        .testTag("payButton")
                        .fillMaxWidth(),
                    onClick =  remember {
                        { onGooglePayButtonClick((viewModel.totalAmount* 100).toFloat()) }
                    },
                    allowedPaymentMethods = PaymentsUtil.allowedPaymentMethods.toString()
                )
//            }
        }
    }
}