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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import androidx.compose.material3.ListItem
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.BuildConfig
import com.example.finalproject.presentation.navigation.MainDestinations
import com.example.finalproject.presentation.onboarding.OnboardingScreen
import com.example.finalproject.utils.FIRST_TIME_LAUNCH
import com.example.finalproject.utils.REMEMBER_ME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @Inject
//    lateinit var shoppingAlarmScheduler: ShoppingAlarmScheduler
    @Inject
    lateinit var preferenceManager: PreferenceManager

    private var hasNotificationPermission: Boolean = false

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        preferenceManager = PreferenceManager(applicationContext)

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
            val startDestination = if (preferenceManager.getData("FIRST_TIME_LAUNCH", true)) {
                MainDestinations.ONBOARDING_ROUTE
            } else {
                MainDestinations.PRODUCT_ROUTE
            }

            ShoppingApp(
                startDestination = startDestination
            )
        }
    }
}