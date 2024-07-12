package com.example.finalproject.presentation.home.profile

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.finalproject.R
import com.example.finalproject.presentation.designsystem.components.ImageCropper
import com.example.finalproject.presentation.designsystem.components.ShoppingScaffold
import com.example.finalproject.presentation.designsystem.components.ShoppingShowToastMessage
import com.example.finalproject.presentation.designsystem.theme.ShoppingAppTheme
import com.example.finalproject.presentation.home.HomeSections
import com.example.finalproject.presentation.home.ShoppingAppBottomBar
import com.example.finalproject.presentation.home.address.rememberMapViewWithLifecycle
import com.example.finalproject.presentation.home.profile.dialog.UpdateAccountInfoDialog
import com.example.finalproject.utils.CustomPreview
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateRoute: (String) -> Unit,
    onSignOutClicked: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var signOut by remember { mutableStateOf(false) }
    var getProfile by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    var isUpdatePasswordVisible by remember { mutableStateOf(false) }
    var isUpdateUsernameVisible by remember { mutableStateOf(false) }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }


    ShoppingScaffold(
        modifier = modifier,
        bottomBar = {
            ShoppingAppBottomBar(
                tabs = HomeSections.values(),
                currentRoute = HomeSections.PROFILE.route,
                navigateToRoute = onNavigateRoute
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {  paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "My Profile",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Profile Content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // First Column - Avatar
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .border(1.dp, Color.Green, CircleShape)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://thumbs.dreamstime.com/b/businessman-icon-vector-male-avatar-profile-image-profile-businessman-icon-vector-male-avatar-profile-image-182095609.jpg")
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Second Column - Profile Information
                Column(
                    modifier = Modifier.weight(0.6f)
                ) {
                    Text(
                        text = "My Name: ${uiState.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "My Email: ${uiState.email}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Email Verified: Yes",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // Buttons
                    OutlinedButton(
                        onClick = { isUpdateUsernameVisible = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        border = null, // Remove default border
                        shape = MaterialTheme.shapes.small, // Rounded corners
                        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                        colors = ButtonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White,
                            disabledContentColor = Color.Gray,
                            disabledContainerColor = Color.DarkGray
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Update Username Icon",
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Update Username",
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { isUpdatePasswordVisible = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = null,
                        shape = MaterialTheme.shapes.small,
                        contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                        colors = ButtonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White,
                            disabledContentColor = Color.Gray,
                            disabledContainerColor = Color.DarkGray
                        )

                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Update Password Icon",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Text(
                                text = "Update Password",
                                modifier = Modifier.padding(start = 8.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                        }
                    }



                }
            }

            if (isUpdateUsernameVisible) {

                Spacer(modifier = Modifier.height(16.dp))

                TextField(

                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("New Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = {
//                                viewModel.updateUsername(newUsername)
                        isUpdateUsernameVisible = false
                    },
                    // ...
                ) {
                    Text("Submit")
                }
            }

            if (isUpdatePasswordVisible) {
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Old Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = {
//                                viewModel.updatePassword(oldPassword, newPassword)
                        isUpdatePasswordVisible = false
                    },
                    // ...
                ) {
                    Text("Submit")
                }
            }
        }
    }

    if (signOut) {
        LaunchedEffect(Unit) {
            viewModel.logOut()
            onSignOutClicked()
        }
    }
    if (getProfile) {
        LaunchedEffect(Unit) {
            viewModel.getUserData()
        }
    }

}