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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.core.notification.ShoppingNotifier
import com.example.finalproject.presentation.navigation.MainDestinations
import com.example.finalproject.utils.ACCESS_TOKEN
import com.example.finalproject.utils.FIRST_TIME_LAUNCH
import com.example.finalproject.utils.REMEMBER_ME
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

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
                Log.d("TAG", "yes")
            }

            Log.d("permission", "${ hasNotificationPermission }")

            val startDestination = if (preferenceManager.getData(FIRST_TIME_LAUNCH, true)) {
                MainDestinations.ONBOARDING_ROUTE
            } else {
//                if (preferenceManager.getData(ACCESS_TOKEN, "").isEmpty()) {
//                    MainDestinations.LOGIN_ROUTE
//                } else {
//                    MainDestinations.PRODUCT_ROUTE
//                }
                MainDestinations.PRODUCT_ROUTE
            }

            ShoppingApp(
                startDestination = startDestination
            )
        }
    }
}