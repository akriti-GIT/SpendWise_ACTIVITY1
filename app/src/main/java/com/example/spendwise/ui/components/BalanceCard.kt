package com.example.spendwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.model.TimeRange
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.CurrencyUtils

@Composable
fun BalanceCard(
    balance: Double,
    selectedRange: TimeRange,
    onRangeSelected: (TimeRange) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(DarkPine)
            .padding(22.dp)
    ) {
        Column {
            Text(
                text = "BALANCE LEFT THIS MONTH",
                fontFamily = SansFamily,
                fontSize = 9.5.sp,
                letterSpacing = 2.0.sp,
                color = Color.White.copy(alpha = 0.55f),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = CurrencyUtils.formatAmount(balance),
                fontFamily = SerifFamily,
                fontSize = 44.sp,
                lineHeight = 48.sp,
                color = CardLight
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "↑ 8.5%",
                    fontFamily = SansFamily,
                    fontSize = 11.sp,
                    color = MintGreen,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "vs last month",
                    fontFamily = SansFamily,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.45f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sparkline Curve
            SparklineAreaChart()

            Spacer(modifier = Modifier.height(6.dp))

            // Time range selector pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TimeRange.values().forEach { range ->
                    val isSelected = range == selectedRange
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color.White.copy(alpha = 0.16f)
                                else Color.Transparent
                            )
                            .clickable { onRangeSelected(range) }
                            .padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = range.label,
                            fontSize = 11.sp,
                            fontFamily = SansFamily,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) CardLight else Color.White.copy(alpha = 0.45f)
                        )
                    }
                }
            }
        }
    }
}
