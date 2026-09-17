package com.example.spendwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.ui.navigation.Screen
import com.example.spendwise.ui.theme.*

@Composable
fun SpendWiseBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Divider top
        HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home
            BottomNavItem(
                icon = Icons.Outlined.Home,
                label = "HOME",
                isSelected = currentRoute == Screen.Home.route,
                onClick = { onNavigate(Screen.Home.route) },
                modifier = Modifier.weight(1f)
            )

            // 2. Activity / Transactions
            BottomNavItem(
                icon = Icons.Outlined.ReceiptLong,
                label = "ACTIVITY",
                isSelected = currentRoute == Screen.Transactions.route,
                onClick = { onNavigate(Screen.Transactions.route) },
                modifier = Modifier.weight(1f)
            )

            // 3. Center Prominent Add Button
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .shadow(elevation = 8.dp, shape = CircleShape, spotColor = DarkPine)
                        .clip(CircleShape)
                        .background(DarkPine)
                        .clickable { onNavigate(Screen.AddTransaction.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Transaction",
                        tint = CardLight,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // 4. Insights / Analytics
            BottomNavItem(
                icon = Icons.Outlined.BarChart,
                label = "INSIGHTS",
                isSelected = currentRoute == Screen.Analytics.route,
                onClick = { onNavigate(Screen.Analytics.route) },
                modifier = Modifier.weight(1f)
            )

            // 5. Settings
            BottomNavItem(
                icon = Icons.Outlined.Settings,
                label = "SETTINGS",
                isSelected = currentRoute == Screen.Settings.route,
                onClick = { onNavigate(Screen.Settings.route) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeColor = if (MaterialTheme.colorScheme.background == DarkBg) MintGreen else DarkPine
    val inactiveColor = TextSecondary.copy(alpha = 0.5f)

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 8.5.sp,
            fontFamily = SansFamily,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            letterSpacing = 1.0.sp,
            color = if (isSelected) activeColor else inactiveColor
        )
    }
}
