package com.example.spendwise.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.model.PeriodType
import com.example.spendwise.ui.components.CategoryDonutChart
import com.example.spendwise.ui.components.WeeklyBarChart
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.CurrencyUtils
import com.example.spendwise.viewmodel.SpendWiseViewModel

@Composable
fun AnalyticsScreen(
    viewModel: SpendWiseViewModel,
    modifier: Modifier = Modifier
) {
    val period by viewModel.analyticsPeriod.collectAsState()
    val periodTotal by viewModel.periodExpensesTotal.collectAsState()
    val categoryBreakdown by viewModel.categorySpending.collectAsState()
    val weeklyDays by viewModel.weeklySpendingDays.collectAsState()
    val topSpendingMessage by viewModel.topSpendingCategory.collectAsState()

    val dailyAverage = if (periodTotal > 0) periodTotal / 30.0 else 0.0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "WHERE IT WENT",
                    fontFamily = SansFamily,
                    fontSize = 9.5.sp,
                    letterSpacing = 2.0.sp,
                    color = Terracotta,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Insights",
                    fontFamily = SerifFamily,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Period Selector Tabs (Week, Month, Year)
        item {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PillBg)
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    PeriodType.values().forEach { p ->
                        val isSelected = p == period
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(if (isSelected) CardLight else Color.Transparent)
                                .clickable { viewModel.setAnalyticsPeriod(p) }
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = p.label,
                                fontFamily = SansFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Donut Chart Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    // Donut Chart Canvas
                    CategoryDonutChart(
                        categories = categoryBreakdown,
                        modifier = Modifier.size(130.dp)
                    )

                    // Details
                    Column {
                        Text(
                            text = "SPENT IN ${period.label.uppercase()}",
                            fontFamily = SansFamily,
                            fontSize = 9.sp,
                            letterSpacing = 1.8.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CurrencyUtils.formatAmount(periodTotal),
                            fontFamily = SerifFamily,
                            fontSize = 30.sp,
                            lineHeight = 32.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "↓ 12% vs last ${period.label.lowercase()}",
                            fontSize = 11.sp,
                            fontFamily = SansFamily,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Daily average ${CurrencyUtils.formatAmount(dailyAverage)}",
                            fontSize = 11.sp,
                            fontFamily = SansFamily,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Category Breakdown List
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
                    if (categoryBreakdown.isEmpty()) {
                        Text(
                            text = "No expenses recorded for this period.",
                            fontSize = 13.sp,
                            fontFamily = SansFamily,
                            color = TextSecondary
                        )
                    } else {
                        categoryBreakdown.forEach { cat ->
                            val barColor = try {
                                Color(android.graphics.Color.parseColor(cat.colorHex))
                            } catch (e: Exception) {
                                Terracotta
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = cat.categoryName,
                                        fontSize = 12.5.sp,
                                        fontFamily = SansFamily,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${CurrencyUtils.formatAmount(cat.totalAmount)} · ${String.format("%.0f", cat.percentage)}%",
                                        fontSize = 12.5.sp,
                                        fontFamily = SansFamily,
                                        color = TextSecondary
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(99.dp))
                                        .background(TrackBg)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(cat.percentage / 100f)
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(99.dp))
                                            .background(barColor)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Weekly Bar Chart Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "THIS WEEK",
                        fontFamily = SansFamily,
                        fontSize = 9.sp,
                        letterSpacing = 1.8.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    WeeklyBarChart(days = weeklyDays)
                }
            }
        }

        // Highlight Summary Card (Pine Green callout)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(DarkPine)
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "✦",
                        fontSize = 16.sp,
                        color = MintGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = topSpendingMessage,
                        fontFamily = SerifFamily,
                        fontSize = 19.sp,
                        lineHeight = 24.sp,
                        color = CardLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Keeping discretionary daily spending capped within your daily target will comfortably save ₹1,500 by month-end.",
                        fontFamily = SansFamily,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp,
                        color = Color.White.copy(alpha = 0.55f)
                    )
                }
            }
        }
    }
}
