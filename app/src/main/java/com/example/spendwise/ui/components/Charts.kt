package com.example.spendwise.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.model.CategorySpending
import com.example.spendwise.model.DailySpending
import com.example.spendwise.ui.theme.*

@Composable
fun SparklineAreaChart(
    points: List<Float> = listOf(58f, 52f, 26f, 30f, 34f, 58f, 54f, 50f, 18f, 20f, 22f, 42f, 34f, 26f),
    lineColor: Color = Color(0xFFE8E2D4),
    fillColor: Color = Color(0xFFC6D4BC),
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(76.dp)
) {
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(points) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, animationSpec = tween(700))
    }

    Canvas(modifier = modifier) {
        if (points.isEmpty()) return@Canvas
        val width = size.width
        val height = size.height
        val minVal = points.minOrNull() ?: 0f
        val maxVal = points.maxOrNull() ?: 100f
        val range = if (maxVal - minVal == 0f) 1f else maxVal - minVal

        val stepX = width / (points.size - 1)

        val coords = points.mapIndexed { idx, value ->
            val normY = 1f - ((value - minVal) / range)
            val padding = height * 0.15f
            val y = padding + normY * (height * 0.7f)
            Offset(idx * stepX, y)
        }

        val path = Path()
        val fillPath = Path()

        coords.forEachIndexed { i, pt ->
            if (i == 0) {
                path.moveTo(pt.x, pt.y)
                fillPath.moveTo(pt.x, height)
                fillPath.lineTo(pt.x, pt.y)
            } else {
                val prev = coords[i - 1]
                val cx1 = prev.x + (pt.x - prev.x) / 2
                val cy1 = prev.y
                val cx2 = prev.x + (pt.x - prev.x) / 2
                val cy2 = pt.y
                path.cubicTo(cx1, cy1, cx2, cy2, pt.x, pt.y)
                fillPath.cubicTo(cx1, cy1, cx2, cy2, pt.x, pt.y)
            }
        }

        fillPath.lineTo(width, height)
        fillPath.close()

        // Draw gradient fill
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(fillColor.copy(alpha = 0.38f * animProgress.value), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )

        // Draw line
        drawPath(
            path = path,
            color = lineColor.copy(alpha = animProgress.value),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Draw end dot
        coords.lastOrNull()?.let { last ->
            drawCircle(
                color = CreamBg,
                radius = 3.5.dp.toPx(),
                center = last
            )
        }
    }
}

@Composable
fun CategoryDonutChart(
    categories: List<CategorySpending>,
    modifier: Modifier = Modifier.size(132.dp),
    strokeWidth: Float = 36f
) {
    val total = categories.sumOf { it.totalAmount }
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(categories) {
        animProgress.snapTo(0f)
        animProgress.animateTo(1f, animationSpec = tween(800))
    }

    Canvas(modifier = modifier) {
        val diameter = size.minDimension
        val radius = (diameter - strokeWidth) / 2
        val centerOffset = Offset(size.width / 2, size.height / 2)

        // Background track circle
        drawCircle(
            color = Color(0xFFEDEAE0),
            radius = radius,
            center = centerOffset,
            style = Stroke(width = strokeWidth)
        )

        if (total <= 0.0) return@Canvas

        var startAngle = -90f
        categories.forEach { item ->
            val sweep = ((item.totalAmount / total) * 360f).toFloat() * animProgress.value
            val segmentColor = try {
                Color(android.graphics.Color.parseColor(item.colorHex))
            } catch (e: Exception) {
                Terracotta
            }

            drawArc(
                color = segmentColor,
                startAngle = startAngle,
                sweepAngle = sweep - 2f, // Subtle gap between segments
                useCenter = false,
                topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
            )
            startAngle += sweep
        }
    }
}

@Composable
fun WeeklyBarChart(
    days: List<DailySpending>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(100.dp),
    barColor: Color = PaleGreen,
    peakColor: Color = Terracotta
) {
    val maxVal = days.maxOfOrNull { it.amount } ?: 1.0
    val safeMax = if (maxVal <= 0.0) 1.0 else maxVal

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        days.forEach { item ->
            val heightRatio = (item.amount / safeMax).coerceIn(0.15, 1.0).toFloat()
            val isPeak = item.isPeak || (item.amount == maxVal && maxVal > 0.0)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // The Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.68f)
                        .fillMaxHeight(heightRatio * 0.78f)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = if (isPeak) peakColor else barColor,
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                        )
                    }
                }
                Spacer(modifier = Modifier.height(7.dp))
                // Day Label
                Text(
                    text = item.dayLabel,
                    fontSize = 10.sp,
                    fontFamily = SansFamily,
                    fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Normal,
                    color = if (isPeak) MaterialTheme.colorScheme.onBackground else TextSecondary
                )
            }
        }
    }
}
