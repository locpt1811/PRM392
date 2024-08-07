package com.example.finalproject.presentation.chat
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Panorama
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.finalproject.R
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    userId: String,
    otherUserId: String,
    onBackClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    // State for messages and error message
    val messages by viewModel.messages.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val otherUserName by viewModel.otherUserName.collectAsState()

    // State for input message and image path
    var messageContent by remember { mutableStateOf(TextFieldValue()) }
    var imageUri by remember { mutableStateOf<Uri?>(null) } // Store selected image URI

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Scroll to the bottom whenever messages or errorMessage changes
    LaunchedEffect(messages, errorMessage) {
        // Scroll to the bottom
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                messagesState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = otherUserName ?: stringResource(id = R.string.chat),
                        color = Color.White
                ) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // Display error message if present
            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = rememberLazyListState().apply {
                    messagesState = this
                }
            ) {
                items(messages.size) { index ->
                    val message = messages[index]
                    val isUserMessage = message.from_user_id == userId
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = if (isUserMessage) Arrangement.End else Arrangement.Start
                    ) {
                        // Display text message
                        if (!message.is_image) {
                            Text(
                                text = message.content,
                                modifier = Modifier
                                    .background(
                                        color = if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                        shape = MaterialTheme.shapes.medium
                                    )
                                    .padding(8.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            // Display image if it's an image message
                            Image(
                                painter = rememberImagePainter(message.content),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(shape = MaterialTheme.shapes.medium)
                            )
                        }
                    }
                }
            }

            // Row for input field, image selection, and send button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Input field for text message
                BasicTextField(
                    value = messageContent,
                    onValueChange = { messageContent = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .border(
                            BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(8.dp)
                )

                // Button to pick image from gallery
                ImageSelection { selectedImageUri ->
                    imageUri = selectedImageUri
                    // Read bytes and send immediately
                    val imageBytes = readBytesFromUri(context, imageUri!!)
                    if (imageBytes != null) {
                        viewModel.sendImageMessage(imageBytes)
                    }
                    imageUri = null // Clear selected image
                }

                // Button to send text message
//                Button(
//                    onClick = {
//                        if (messageContent.text.isNotEmpty()) {
//                            viewModel.sendMessage(messageContent.text)
//                            messageContent = TextFieldValue() // Clear the input field
//                        }
//                    }
//                ) {
//                    Text("Send")
//                }

                IconButton(
                    onClick = {
                        if (messageContent.text.isNotEmpty()) {
                            viewModel.sendMessage(messageContent.text)
                            messageContent = TextFieldValue() // Clear the input field
                        }
                    }
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        }
    }
}

@Composable
fun ImageSelection(
    onImageSelected: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            onImageSelected(it)
        }
    }

//    Button(
//        onClick = {
//            launcher.launch("image/*")
//        }
//    ) {
//        Text("Pick Image")
//    }

    IconButton(onClick = { launcher.launch("image/*") }) {
        Icon(imageVector = Icons.Filled.Panorama, contentDescription = "Picture")
    }

}

private fun readBytesFromUri(context: Context, uri: Uri): ByteArray? {
    val contentResolver: ContentResolver = context.contentResolver
    val inputStream: InputStream? = contentResolver.openInputStream(uri)
    val byteBuffer = ByteArrayOutputStream()

    inputStream?.use {
        val buffer = ByteArray(1024)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
    }

    return byteBuffer.toByteArray()
}
// Outside the ChatScreen composable
private var messagesState: LazyListState by mutableStateOf(LazyListState())
