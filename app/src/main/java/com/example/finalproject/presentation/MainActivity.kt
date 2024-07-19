package com.example.finalproject.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.finalproject.R
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.core.notification.ShoppingNotifier
import com.example.finalproject.presentation.cart.CartViewModel
import com.example.finalproject.presentation.designsystem.components.ShoppingButton
import com.example.finalproject.presentation.designsystem.components.SuccessPay
import com.example.finalproject.presentation.navigation.MainDestinations
import com.example.finalproject.utils.ACCESS_TOKEN
import com.example.finalproject.utils.FIRST_TIME_LAUNCH
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.contract.TaskResultContracts
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @Inject
//    lateinit var shoppingAlarmScheduler: ShoppingAlarmScheduler

    @Inject
    lateinit var shoppingNotifier: ShoppingNotifier

    @Inject
    lateinit var preferenceManager: PreferenceManager

    private var hasNotificationPermission: Boolean = false

    private val viewModel: MainActivityViewModel by viewModels()
    private val model: CartViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashWasDisplayed = savedInstanceState != null
        if(!splashWasDisplayed){
            val splashScreen = installSplashScreen()
            splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
                splashScreenViewProvider.iconView
                    .animate()
                    .setDuration(3000L)
                    .alpha(0f)
                    .scaleX(3f)
                    .scaleY(3f)
                    .withEndAction {
                        splashScreenViewProvider.remove()
                    }.start()
            }
        }

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val cartUiState by model.uiState.collectAsState()

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { hasNotificationPermission = it }
            )

            if (!hasNotificationPermission) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    SideEffect {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

//            if (uiState.consumableViewEvent.isNotEmpty()) {
//                shoppingAlarmScheduler.schedule()
//                viewModel.onUiEventConsumed()
//            }
            if (hasNotificationPermission) {
                shoppingNotifier.launchNotification()
            }

            val startDestination =
                if (preferenceManager.getData(FIRST_TIME_LAUNCH, true)) {

                    MainDestinations.ONBOARDING_ROUTE

            } else {
                    Log.d("ShowInit", preferenceManager.getData(ACCESS_TOKEN, "").toString())
                    if(preferenceManager.getData(ACCESS_TOKEN, "").toString().isEmpty()){
                        MainDestinations.LOGIN_ROUTE
                    }else{
                        MainDestinations.PRODUCT_ROUTE
                    }

            }
            ShoppingApp(
                startDestination = startDestination,
                mainActivity = this@MainActivity
            )
        }
    }

    private val paymentDataLauncher = registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    Log.i("Google Pay result:", it.toJson())
                    model.setPaymentData(it)
                    val intent = Intent(this, PaymentActivity::class.java)
                    startActivity(intent)
                }
            }
            //CommonStatusCodes.CANCELED -> The user canceled
            //AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
            //CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
        }
    }

    fun requestPayment(amount: Long) {
        val task = model.getLoadPaymentDataTask(amount)
        task.addOnCompleteListener(paymentDataLauncher::launch)
    }
}