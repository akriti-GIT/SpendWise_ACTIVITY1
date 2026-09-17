package com.example.spendwise.ui.screens.addtransaction

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.model.PaymentMethod
import com.example.spendwise.model.TransactionType
import com.example.spendwise.ui.components.CustomKeypad
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.DateUtils
import com.example.spendwise.viewmodel.SpendWiseViewModel
import java.util.Calendar

@Composable
fun AddTransactionScreen(
    viewModel: SpendWiseViewModel,
    onNavigateBack: () -> Unit,
    editingTransaction: TransactionEntity? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedType by remember { mutableStateOf(editingTransaction?.type ?: TransactionType.EXPENSE) }
    var amountText by remember { mutableStateOf(editingTransaction?.let { String.format("%.2f", it.amount) } ?: "") }
    var selectedCategory by remember { mutableStateOf(editingTransaction?.category ?: "Food") }
    var descriptionText by remember { mutableStateOf(editingTransaction?.description ?: "") }
    var selectedPaymentMethod by remember { mutableStateOf(editingTransaction?.paymentMethod ?: PaymentMethod.UPI) }
    var selectedDate by remember { mutableStateOf(editingTransaction?.date ?: System.currentTimeMillis()) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val categories = listOf("Food", "Transport", "Education", "Shopping", "Entertainment", "Health", "Bills", "Other")

    val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate }
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val cal = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                selectedDate = cal.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            errorMessage = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar: Cancel, Title, Date Picker button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cancel",
                    fontFamily = SansFamily,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.clickable { onNavigateBack() }
                )
                Text(
                    text = if (editingTransaction != null) "EDIT ENTRY" else "NEW ENTRY",
                    fontFamily = SansFamily,
                    fontSize = 9.5.sp,
                    letterSpacing = 2.0.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { datePickerDialog.show() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = "Select Date",
                        tint = Terracotta,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = DateUtils.getRelativeDateHeader(selectedDate),
                        fontFamily = SansFamily,
                        fontSize = 12.sp,
                        color = Terracotta,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Transaction Type Toggle (Expense / Income)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PillBg)
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.width(220.dp)) {
                    listOf(TransactionType.EXPENSE, TransactionType.INCOME).forEach { type ->
                        val isSelected = selectedType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(if (isSelected) DarkPine else Color.Transparent)
                                .clickable { selectedType = type }
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type.title,
                                fontFamily = SansFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) CardLight else TextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Input Display
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "₹",
                    fontFamily = SerifFamily,
                    fontSize = 32.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (amountText.isEmpty()) "0" else amountText,
                    fontFamily = SerifFamily,
                    fontSize = 56.sp,
                    lineHeight = 60.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Description / Note field
            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                placeholder = { Text("Note (e.g. Campus Cafeteria, Books, Metro)", fontSize = 12.5.sp, textAlign = TextAlign.Center) },
                singleLine = true,
                shape = RoundedCornerShape(99.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = DarkPine,
                    unfocusedBorderColor = CardBorder
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Category Selector Grid (8 categories)
            Text(
                text = "SELECT CATEGORY",
                fontFamily = SansFamily,
                fontSize = 9.sp,
                letterSpacing = 1.8.sp,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 4x2 Grid for categories
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                categories.chunked(4).forEach { rowCategories ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        rowCategories.forEach { cat ->
                            val isSelected = selectedCategory.equals(cat, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(13.dp))
                                    .background(if (isSelected) DarkPine else MaterialTheme.colorScheme.surface)
                                    .border(1.dp, if (isSelected) DarkPine else CardBorder, RoundedCornerShape(13.dp))
                                    .clickable { selectedCategory = cat }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontFamily = SansFamily,
                                    color = if (isSelected) CardLight else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Payment Method Selector
            Text(
                text = "PAYMENT METHOD",
                fontFamily = SansFamily,
                fontSize = 9.sp,
                letterSpacing = 1.8.sp,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaymentMethod.values().forEach { method ->
                    val isSelected = selectedPaymentMethod == method
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) DarkPine else MaterialTheme.colorScheme.surface)
                            .border(1.dp, if (isSelected) DarkPine else CardBorder, RoundedCornerShape(12.dp))
                            .clickable { selectedPaymentMethod = method }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = method.label,
                            fontFamily = SansFamily,
                            fontSize = 11.sp,
                            color = if (isSelected) CardLight else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Numeric Keypad
            CustomKeypad(
                onKeyPressed = { key ->
                    when (key) {
                        "del" -> {
                            if (amountText.isNotEmpty()) amountText = amountText.dropLast(1)
                        }
                        "." -> {
                            if (!amountText.contains(".")) amountText = if (amountText.isEmpty()) "0." else "$amountText."
                        }
                        else -> {
                            if (amountText == "0") {
                                amountText = key
                            } else if (amountText.contains(".")) {
                                val decimals = amountText.split(".")[1]
                                if (decimals.length < 2) amountText += key
                            } else if (amountText.length < 7) {
                                amountText += key
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    val amountVal = amountText.toDoubleOrNull() ?: 0.0
                    if (amountVal <= 0.0) {
                        errorMessage = "Please enter a valid amount greater than ₹0"
                        return@Button
                    }
                    if (selectedCategory.isBlank()) {
                        errorMessage = "Please select a category"
                        return@Button
                    }

                    if (editingTransaction != null) {
                        viewModel.updateTransaction(
                            editingTransaction.copy(
                                type = selectedType,
                                amount = amountVal,
                                category = selectedCategory,
                                description = descriptionText.ifBlank { selectedCategory },
                                date = selectedDate,
                                paymentMethod = selectedPaymentMethod
                            ),
                            onSuccess = { onNavigateBack() },
                            onError = { errorMessage = it }
                        )
                    } else {
                        viewModel.addTransaction(
                            type = selectedType,
                            amount = amountVal,
                            category = selectedCategory,
                            description = descriptionText.ifBlank { selectedCategory },
                            date = selectedDate,
                            paymentMethod = selectedPaymentMethod,
                            onSuccess = { onNavigateBack() },
                            onError = { errorMessage = it }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(99.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkPine)
            ) {
                Text(
                    text = if (selectedType == TransactionType.EXPENSE) "Save expense" else "Save income",
                    fontFamily = SansFamily,
                    fontSize = 14.sp,
                    color = CardLight,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
