package com.example.finalproject.presentation.chat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatScreen(chatRoomId: String, viewModel: ChatViewModel = hiltViewModel()) {
    val messages by viewModel.messages.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var messageContent by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(chatRoomId) {
        viewModel.fetchMessages(chatRoomId)
        viewModel.listenToMessages(chatRoomId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(count = messages.size) { index ->
                val message = messages[index]
                Text(text = message.content, modifier = Modifier.padding(8.dp))
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = messageContent,
                onValueChange = { messageContent = it },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )

            Button(onClick = {
                if (messageContent.text.isNotEmpty()) {
                    viewModel.sendMessage(chatRoomId, messageContent.text)
                    messageContent = TextFieldValue() // Clear the input field
                }
            }) {
                Text("Send")
            }
        }
    }
}

