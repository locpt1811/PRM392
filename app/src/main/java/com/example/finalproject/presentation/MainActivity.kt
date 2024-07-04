package com.example.finalproject.presentation


import android.Manifest
import android.os.Build
import android.os.Bundle
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
import com.example.finalproject.model.shopping.BookDTO
import com.example.finalproject.common.helper.PreferenceManager
import com.example.finalproject.BuildConfig
import com.example.finalproject.presentation.navigation.MainDestinations
import com.example.finalproject.utils.REMEMBER_ME
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
) {
    install(Auth)
    install(Postgrest)
    //install other modules
}

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

            ShoppingApp(
                startDestination = if (preferenceManager.getData(
                        REMEMBER_ME,
                        false
                    )
                ) MainDestinations.PRODUCT_ROUTE
                else MainDestinations.LOGIN_ROUTE
            )
        }
    }
}