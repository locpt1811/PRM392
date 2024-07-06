package com.example.finalproject.presentation.home.address


import android.os.Bundle
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color


import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import com.example.finalproject.presentation.designsystem.components.ShoppingScaffold

import com.example.finalproject.presentation.home.HomeSections
import com.example.finalproject.presentation.home.ShoppingAppBottomBar



import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    onNavigateRoute: (String) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.ADDRESS.route,
                navigateToRoute = onNavigateRoute
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .fillMaxWidth()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "The location of our shop is in Landmark 81",
                    textAlign = TextAlign.Left,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            val mapView = rememberMapViewWithLifecycle()
            AndroidView({ mapView }) { mapView ->
                mapView.getMapAsync { googleMap ->
                    val shopLocation = LatLng(10.7942, 106.7214) // Landmark 81, Vietnam
                    googleMap.addMarker(MarkerOptions().position(shopLocation).title("Marker in my shop"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(shopLocation))
                }
            }

        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }
    DisposableEffect(Unit) {
        mapView.onCreate(Bundle())
        mapView.onStart()
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }
    return mapView
}