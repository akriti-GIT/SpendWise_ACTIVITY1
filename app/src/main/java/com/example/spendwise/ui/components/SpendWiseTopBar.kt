package com.example.spendwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.ui.theme.*
import com.example.spendwise.util.DateUtils

@Composable
fun SpendWiseTopBar(
    studentName: String = "Maya Chen",
    modifier: Modifier = Modifier
) {
    val initial = studentName.firstOrNull()?.uppercase() ?: "S"
    val now = System.currentTimeMillis()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Circle
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PaleGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                fontFamily = SerifFamily,
                fontSize = 19.sp,
                color = DarkPine,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Student Greeting
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "GOOD MORNING",
                fontFamily = SansFamily,
                fontSize = 9.5.sp,
                letterSpacing = 1.8.sp,
                color = Terracotta,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = studentName,
                fontFamily = SerifFamily,
                fontSize = 21.sp,
                color = TextPrimary,
                lineHeight = 22.sp
            )
        }

        // Date Display
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = DateUtils.formatDayMonth(now).uppercase(),
                fontFamily = SansFamily,
                fontSize = 9.5.sp,
                letterSpacing = 1.6.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = DateUtils.formatDayOfWeek(now).uppercase(),
                fontFamily = SansFamily,
                fontSize = 9.5.sp,
                letterSpacing = 1.6.sp,
                color = TextMuted,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
