package com.example.spendwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.CurrencyUtils

@Composable
fun SummaryCardsRow(
    income: Double,
    expenses: Double,
    incomeSubtitle: String = "Job + allowance",
    expensesCount: Int = 24,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Income Card
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                .padding(horizontal = 15.dp, vertical = 14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "INCOME",
                        fontFamily = SansFamily,
                        fontSize = 9.sp,
                        letterSpacing = 1.8.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(PaleGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "↗",
                            fontSize = 12.sp,
                            color = DarkPine,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = CurrencyUtils.formatAmount(income),
                    fontFamily = SerifFamily,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = incomeSubtitle,
                    fontFamily = SansFamily,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
        }

        // Expenses Card
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                .padding(horizontal = 15.dp, vertical = 14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "SPENT",
                        fontFamily = SansFamily,
                        fontSize = 9.sp,
                        letterSpacing = 1.8.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(PeachLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "↘",
                            fontSize = 12.sp,
                            color = WarmBrown,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = CurrencyUtils.formatAmount(expenses),
                    fontFamily = SerifFamily,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$expensesCount transactions",
                    fontFamily = SansFamily,
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
        }
    }
}
