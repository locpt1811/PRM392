package com.example.finalproject.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel = hiltViewModel(),
    onChatIconClick: (String,String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(uiState.chatList.size) { index ->
            val userId = uiState.userId
            val otherUserId = uiState.chatList[index]
            if(userId!=null) {
                ChatUserIcon(
                    onClick = {
                            onChatIconClick(userId, otherUserId)
                    },
                    userId = userId
                )
            }
        }
    }
}

@Composable
fun ChatUserIcon(
    userId: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .padding(8.dp)
            .clickable { onClick() }
            .background(Color.Gray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = userId.first().uppercaseChar().toString(), color = Color.White)
    }
}
