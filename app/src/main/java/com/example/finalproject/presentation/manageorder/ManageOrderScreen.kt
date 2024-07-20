package com.example.finalproject.presentation.manageorder

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.finalproject.model.shopping.OrderDTO
import com.example.finalproject.model.shopping.OrderStatusDTO
import com.example.finalproject.presentation.designsystem.components.ShoppingShowToastMessage
import com.example.finalproject.presentation.myorder.MyOrdersViewModel
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ManageOrderScreen(
    viewModel: ManageOrderViewModel = hiltViewModel(),
    onOrderDetailClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    val orders by viewModel.orders.collectAsState()
    val statuses by viewModel.statuses.collectAsState()
    val userMessages by viewModel.userMessages.collectAsState()

    // State for dialog visibility
    var showDialog by remember { mutableStateOf(false) }
    var selectedOrderId by remember { mutableStateOf<Int?>(null) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }

    if (userMessages.isNotEmpty()) {
        ShoppingShowToastMessage(message = userMessages.first())
        viewModel.consumedUserMessages()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Manage Orders") },
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
        if(viewModel.fetchCurrentUser()) {
            Text(
                text ="You do not have permission to access this page",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                ),
            )
        }else
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No orders found.", style = MaterialTheme.typography.headlineSmall)
            }
        }
        else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(orders.size) { index ->
                    val order = orders[index]
                    val orderId = order.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ClickableText(
                                text = AnnotatedString("Order ID: $orderId"),
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                onClick = { onOrderDetailClick(orderId) }
                            )
                            Text(
                                text = "Current Status: ${order.status}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                            )

                            Text(
                                text = "User: ${order.user_id}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Address: ${order.address}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Order Date: ${order.created_at}",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            var totalItems = 0
                            var totalAmount = 0.0
                            order.items.forEach { item ->
                                val itemTotal = item.book.price?.times(item.quantity)
                                if (itemTotal != null) {
                                    totalAmount += itemTotal
                                }
                                totalItems += item.quantity
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Total Items: $totalItems",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Total Amount: $${"%.2f".format(totalAmount)}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )

                            // Button to show status selection dialog
                            Button(
                                onClick = {
                                    selectedOrderId = orderId
                                    selectedStatus = order.status
                                    showDialog = true
                                }
                            ) {
                                Text("Change Status")
                            }
                        }
                    }
                }
            }
        }


        // Show dialog when showDialog is true
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = { Text("Select Status") },
                text = {
                    Column {
                        statuses.forEach { status ->
                            TextButton(
                                onClick = {
                                    selectedStatus?.let { newStatus ->
                                        viewModel.updateOrderStatus(selectedOrderId ?: return@let, status.status_id ?: 0)
                                        showDialog = false
                                    }
                                }
                            ) {
                                Text(status.status_value ?: "")
                            }
                        }
                    }
                },
                confirmButton = {
//                    Button(
//                        onClick = {
//                            selectedStatus?.let { newStatus ->
//                                viewModel.updateOrderStatus(selectedOrderId ?: return@let, statuses.find { it.status_value == newStatus }?.status_id ?: 0)
//                                showDialog = false
//                            }
//                        }
//                    ) {
//                        Text("Update")
//                    }
                },
                dismissButton = {
                    Button(
                        modifier = Modifier,
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
