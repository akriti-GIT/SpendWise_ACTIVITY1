package com.example.spendwise.ui.screens.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.model.TransactionType
import com.example.spendwise.ui.components.ConfirmationDialog
import com.example.spendwise.ui.components.getCategoryColor
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.CurrencyUtils
import com.example.spendwise.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailSheet(
    transaction: TransactionEntity?,
    onDismiss: () -> Unit,
    onEdit: (TransactionEntity) -> Unit,
    onDelete: (TransactionEntity) -> Unit
) {
    if (transaction == null) return

    var showDeleteConfirm by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category Icon Dot
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(getCategoryColor(transaction.category))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = transaction.category.uppercase(),
                fontFamily = SansFamily,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            val isExpense = transaction.type == TransactionType.EXPENSE
            val sign = if (isExpense) "−" else "+"
            Text(
                text = "$sign${CurrencyUtils.formatAmount(transaction.amount)}",
                fontFamily = SerifFamily,
                fontSize = 36.sp,
                color = if (isExpense) MaterialTheme.colorScheme.onSurface else SuccessGreen,
                fontWeight = FontWeight.Medium
            )

            if (transaction.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = transaction.description,
                    fontFamily = SansFamily,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(color = CardBorder)

            Spacer(modifier = Modifier.height(16.dp))

            // Details rows
            DetailRow(label = "Date", value = DateUtils.formatDate(transaction.date))
            DetailRow(label = "Time", value = DateUtils.formatTime(transaction.date))
            DetailRow(label = "Payment Method", value = transaction.paymentMethod.label)
            DetailRow(label = "Transaction Type", value = transaction.type.title)

            Spacer(modifier = Modifier.height(24.dp))

            // Actions: Edit and Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onEdit(transaction)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(99.dp)
                ) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit", fontFamily = SansFamily)
                }

                Button(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    shape = RoundedCornerShape(99.dp)
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Delete", fontFamily = SansFamily)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showDeleteConfirm) {
        ConfirmationDialog(
            title = "Delete Transaction?",
            message = "Are you sure you want to delete this transaction of ${CurrencyUtils.formatAmount(transaction.amount)}? This action cannot be undone.",
            confirmText = "Delete",
            onConfirm = {
                showDeleteConfirm = false
                onDismiss()
                onDelete(transaction)
            },
            onDismiss = { showDeleteConfirm = false }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontFamily = SansFamily,
            fontSize = 13.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontFamily = SansFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
