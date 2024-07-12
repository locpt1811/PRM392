package com.example.finalproject.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.finalproject.R
import com.example.finalproject.presentation.designsystem.components.AuthBackground
import com.example.finalproject.presentation.designsystem.components.AuthEnterEmailOtf
import com.example.finalproject.presentation.designsystem.components.AuthEnterPasswordOtf
import com.example.finalproject.presentation.designsystem.components.FullScreenCircularLoading
import com.example.finalproject.presentation.designsystem.components.ShoppingButton
import com.example.finalproject.presentation.designsystem.components.ShoppingScaffold
import com.example.finalproject.presentation.designsystem.components.ShoppingShowToastMessage
import com.example.finalproject.presentation.designsystem.components.WelcomeText
import com.example.finalproject.presentation.designsystem.theme.ShoppingAppTheme
import com.example.finalproject.presentation.navigation.MainDestinations
import com.example.finalproject.utils.CustomPreview

@Composable
fun SignUpScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.errorMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = uiState.errorMessages.first().asString())
        viewModel.consumedErrorMessage()
    }
    val showSnackbar = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(uiState.isSignUpEnd) {
        if (uiState.isSignUpEnd) {
            showSnackbar.value = true
            navController.navigate(MainDestinations.LOGIN_ROUTE) {
                popUpTo(MainDestinations.SIGNUP_ROUTE) { inclusive = true }
            }
        }
    }
    if(showSnackbar.value){
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action =  {
                TextButton(onClick = { showSnackbar.value = false }) {
                    Text("Dismiss")
                }
            }
        ) {
            Text(text = "Visit your email and confirm to login into our app")
        }
    }
    ShoppingScaffold(modifier = modifier) { paddingValues ->
        AuthBackground()
        SignUpScreenContent(
            modifier = Modifier.padding(paddingValues),
            emailValue = viewModel.email,
            onEmailValueChange = remember(viewModel) { viewModel::updateEmailField },
            emailFieldError = uiState.emailFieldErrorMessage != null,
            passwordValue = viewModel.password,
            onPasswordChange = remember(viewModel) { viewModel::updatePasswordField },
            passwordFieldError = uiState.passwordFieldErrorMessage != null,
            verifyPasswordValue = viewModel.verifyPassword,
            onVerifyPasswordChange = remember(viewModel) { viewModel::updateVerifyPasswordField },
            verifyPasswordFieldError = uiState.verifyPasFieldErrorMessage != null,
            emailLabel = uiState.emailFieldErrorMessage?.asString()
                ?: stringResource(id = R.string.enter_email),
            passwordLabel = uiState.passwordFieldErrorMessage?.asString()
                ?: stringResource(id = R.string.enter_password),
            verifyPasswordLabel = uiState.verifyPasFieldErrorMessage?.asString()
                ?: stringResource(id = R.string.verify_password),
            onSignUpClick = remember(viewModel) { { viewModel.signUp(upPress) } },
            isLoading = uiState.isLoading,
            isSignUpEnd = uiState.isSignUpEnd
        )
    }
}

@Composable
private fun SignUpScreenContent(
    modifier: Modifier,
    emailValue: String,
    onEmailValueChange: (String) -> Unit,
    emailFieldError: Boolean,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
    passwordFieldError: Boolean,
    verifyPasswordValue: String,
    onVerifyPasswordChange: (String) -> Unit,
    verifyPasswordFieldError: Boolean,
    emailLabel: String,
    passwordLabel: String,
    verifyPasswordLabel: String,
    onSignUpClick: () -> Unit,
    isLoading: Boolean,
    isSignUpEnd: Boolean
) {
    if (isLoading) {
        FullScreenCircularLoading()
    } else if (!isSignUpEnd) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.two_level_margin))
                .padding(bottom = dimensionResource(id = R.dimen.eight_level_margin)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            WelcomeText(modifier = modifier, text = stringResource(id = R.string.welcome_register))
            AuthEnterEmailOtf(
                value = emailValue,
                onValueChange = onEmailValueChange,
                labelText = emailLabel,
                isError = emailFieldError
            )
            AuthEnterPasswordOtf(
                value = passwordValue,
                onValueChange = onPasswordChange,
                labelText = passwordLabel,
                isError = passwordFieldError
            )
            AuthEnterPasswordOtf(
                value = verifyPasswordValue,
                onValueChange = onVerifyPasswordChange,
                labelText = verifyPasswordLabel,
                isError = verifyPasswordFieldError
            )
            ShoppingButton(
                modifier = modifier.padding(top = dimensionResource(id = R.dimen.one_level_margin)),
                onClick = onSignUpClick,
                buttonText = stringResource(id = R.string.sign_up)
            )
        }
    }
}

@CustomPreview
@Composable
private fun SignUpScreenPreview() {
    ShoppingAppTheme {
        Surface {
            AuthBackground()
            SignUpScreenContent(
                modifier = Modifier,
                emailValue = "",
                onEmailValueChange = {},
                emailFieldError = false,
                passwordValue = "",
                onPasswordChange = {},
                passwordFieldError = false,
                verifyPasswordValue = "",
                onVerifyPasswordChange = {},
                verifyPasswordFieldError = false,
                emailLabel = "Email",
                passwordLabel = "Password",
                verifyPasswordLabel = "VerifyPassword",
                onSignUpClick = {},
                isLoading = false,
                isSignUpEnd = false
            )
        }
    }
}