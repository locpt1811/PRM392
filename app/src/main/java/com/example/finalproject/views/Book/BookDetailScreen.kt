package com.example.finalproject.views.Book

import android.annotation.SuppressLint
import android.net.Uri
import android.text.Layout.Alignment
import android.widget.Button
import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBar
import io.github.jan.supabase.realtime.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

//@OptIn(ExperimentalCoilApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: BookDetailsViewModel = hiltViewModel(),
    navController: NavController,
    productId: String?,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
//                background = MaterialTheme.colorScheme.onPrimary,
                title = {
                    Text(
                        text = "Book detail",
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
            )
        }
    ) {
        val name = viewModel.name.collectAsState(initial = "")
        val price = viewModel.price.collectAsState(initial = 0.0)
//        var imageUrl = Uri.parse(viewModel.imageUrl.collectAsState(initial = null).value)
        val contentResolver = LocalContext.current.contentResolver

        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
//            val galleryLauncher =
//                rememberLauncherForActivityResult(ActivityResultContracts.GetContent())
//                { uri ->
//                    uri?.let {
//                        if (it.toString() != imageUrl.toString()) {
//                            viewModel.onImageChange(it.toString())
//                        }
//                    }
//                }

//            Image(
//                painter = rememberImagePainter(imageUrl),
//                contentScale = ContentScale.Fit,
//                contentDescription = null,
//                modifier = Modifier
//                    .padding(16.dp, 8.dp)
//                    .size(100.dp)
//                    .align(Alignment.CenterHorizontally)
//            )
            IconButton(modifier = modifier.align(alignment = androidx.compose.ui.Alignment.CenterHorizontally),
                onClick = {
//                    galleryLauncher.launch("image/*")
                }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
//            OutlinedTextField(
//                label = {
//                    Text(
//                        text = "Product name",
//                        color = MaterialTheme.colorScheme.primary,
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                },
//                maxLines = 2,
//                shape = RoundedCornerShape(32),
//                modifier = modifier.fillMaxWidth(),
//                value = name.value,
//                onValueChange = {
//                    viewModel.onNameChange(it)
//                },
//            )
            Spacer(modifier = modifier.height(12.dp))
//            OutlinedTextField(
//                label = { Text(text = "Product price") },
//                value = price.value.toString(),
//                onValueChange = {
//                    viewModel.onPriceChange(it.toDoubleOrNull() ?: 0.0)
//                },
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    keyboardType = KeyboardType.Number
//                ),
//                modifier = Modifier
//                    .fillMaxWidth(),
//                maxLines = 1,
//                shape = RoundedCornerShape(8.dp) // Adjust the corner radius as per your design
//            )
//            Spacer(modifier = modifier.weight(1f))
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
//                    if (imageUrl.host?.contains("supabase") == true) {
//                        viewModel.onSaveProduct(image = byteArrayOf())
//                    } else {
//                        val image = uriToByteArray(contentResolver, imageUrl)
//                        viewModel.onSaveProduct(image = image)
//                    }
//                    coroutineScope.launch {
//                        snackBarHostState.showSnackbar(
//                            message = "Product updated successfully !",
//                            duration = SnackbarDuration.Short
//                        )
//                    }
                }) {
                Text(text = "Save changes")
            }
            Spacer(modifier = modifier.height(12.dp))
            OutlinedButton(
                modifier = modifier
                    .fillMaxWidth(),
                onClick = {
                    navController.navigateUp()
                }) {
                Text(text = "Cancel")
            }

        }

    }
}



//private fun getBytes(inputStream: InputStream): ByteArray {
//    val byteBuffer = ByteArrayOutputStream()
//    val bufferSize = 1024
//    val buffer = ByteArray(bufferSize)
//    var len = 0
//    while (inputStream.read(buffer).also { len = it } != -1) {
//        byteBuffer.write(buffer, 0, len)
//    }
//    return byteBuffer.toByteArray()
//}
//
//
//private fun uriToByteArray(contentResolver: ContentResolver, uri: Uri): ByteArray {
//    if (uri == Uri.EMPTY) {
//        return byteArrayOf()
//    }
//    val inputStream = contentResolver.openInputStream(uri)
//    if (inputStream != null) {
//        return getBytes(inputStream)
//    }
//    return byteArrayOf()
//}
