package com.example.finalproject.presentation.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.finalproject.R

@Composable
fun SuccessPay(
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(id = R.dimen.four_level_margin),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin)),
            painter = painterResource(id = R.drawable.payment_success),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.four_level_margin))
                .padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            text = stringResource(id = R.string.payment_success),
            textAlign = TextAlign.Center
        )
        ShoppingButton(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.two_level_margin)),
            onClick = onContinueShopping,
            buttonText = stringResource(id = R.string.continue_shopping)
        )
    }
}