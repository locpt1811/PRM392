package com.example.finalproject.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalproject.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onChatIconClick: (String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.chat_list)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(uiState.chatList.size) { index ->
                val otherUser = uiState.chatList[index]
                ChatUserIcon(
                    onClick = { onChatIconClick(uiState.userId.orEmpty(), otherUser.id) },
                    userName = otherUser.first_name+" "+otherUser.last_name
                )
            }
        }
    }
}@Composable
fun ChatUserIcon(
    userName: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 180.dp, height = 60.dp)
            .padding(8.dp)
            .clickable { onClick() }
            .background(Color(0xFF800080), shape = RoundedCornerShape(8.dp)), // Use TriangleShape for a triangle background
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = userName,
            color = Color.White,
            fontSize = 24.sp, // Adjust text size as needed
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, // Handle overflow with ellipsis
            modifier = Modifier.padding(8.dp) // Add padding around the text
        )
    }
}
