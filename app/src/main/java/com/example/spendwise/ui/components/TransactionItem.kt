package com.example.spendwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.data.entity.TransactionEntity
import com.example.spendwise.model.TransactionType
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.CurrencyUtils
import com.example.spendwise.util.DateUtils

fun getCategoryColor(category: String): Color {
    return when (category.lowercase()) {
        "food" -> Terracotta
        "transport" -> SageGreen
        "education", "school" -> DarkPine
        "shopping", "fun", "entertainment" -> Peach
        "salary", "scholarship", "allowance", "income" -> SuccessGreen
        "health" -> WarmBrown
        "bills" -> SageGreen
        else -> Sand
    }
}

@Composable
fun TransactionItem(
    transaction: TransactionEntity,
    onClick: () -> Unit,
    showDivider: Boolean = true,
    modifier: Modifier = Modifier
) {
    val isExpense = transaction.type == TransactionType.EXPENSE
    val dotColor = getCategoryColor(transaction.category)
    val amountFormatted = CurrencyUtils.formatAmount(transaction.amount)
    val sign = if (isExpense) "−" else "+"
    val amountColor = if (isExpense) MaterialTheme.colorScheme.onBackground else SuccessGreen

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Color Dot
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Description & Meta
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description.ifEmpty { transaction.category },
                    fontSize = 14.sp,
                    fontFamily = SansFamily,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${transaction.category} · ${DateUtils.formatTime(transaction.date)}",
                    fontSize = 10.5.sp,
                    fontFamily = SansFamily,
                    color = TextSecondary
                )
            }

            // Amount
            Text(
                text = "$sign$amountFormatted",
                fontFamily = SerifFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = amountColor
            )
        }

        if (showDivider) {
            HorizontalDivider(
                color = CardBorder,
                thickness = 0.8.dp
            )
        }
    }
}
