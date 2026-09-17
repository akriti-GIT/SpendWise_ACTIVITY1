package com.example.spendwise.ui.screens.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.ui.components.ConfirmationDialog
import com.example.spendwise.ui.theme.*
import com.example.spendwise.viewmodel.SpendWiseViewModel

@Composable
fun SettingsScreen(
    viewModel: SpendWiseViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userSettings by viewModel.userSettings.collectAsState()

    var showClearDataConfirm by remember { mutableStateOf(false) }
    var showResetSampleConfirm by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf("") }
    var editId by remember { mutableStateOf("") }

    val studentName = userSettings?.studentName ?: "Maya Chen"
    val studentId = userSettings?.collegeId ?: "MCA-2026-087"
    val isDark = userSettings?.darkMode ?: false

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
                    text = "PREFERENCES",
                    fontFamily = SansFamily,
                    fontSize = 9.5.sp,
                    letterSpacing = 2.0.sp,
                    color = Terracotta,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Settings",
                    fontFamily = SerifFamily,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Profile Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
                    .clickable {
                        editName = studentName
                        editId = studentId
                        showProfileDialog = true
                    }
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(PaleGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = studentName.firstOrNull()?.uppercase() ?: "S",
                            fontFamily = SerifFamily,
                            fontSize = 24.sp,
                            color = DarkPine,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = studentName,
                            fontFamily = SerifFamily,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Student ID: $studentId",
                            fontFamily = SansFamily,
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Icon(
                        Icons.Outlined.ChevronRight,
                        contentDescription = "Edit Profile",
                        tint = TextMuted
                    )
                }
            }
        }

        // Section: Preferences
        item {
            SectionHeader("APP PREFERENCES")
        }

        item {
            CardContainer {
                // Currency
                SettingRow(
                    icon = Icons.Outlined.CurrencyRupee,
                    title = "Currency",
                    detail = "Indian Rupee (₹)"
                )
                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

                // Dark Mode Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.DarkMode,
                            contentDescription = null,
                            tint = DarkPine,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Dark Mode",
                            fontFamily = SansFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Switch(
                        checked = isDark,
                        onCheckedChange = { viewModel.toggleDarkMode(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = CardLight, checkedTrackColor = DarkPine)
                    )
                }

                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

                // Notifications
                SettingRow(
                    icon = Icons.Outlined.Notifications,
                    title = "Budget Alerts",
                    detail = "Enabled"
                )
            }
        }

        // Section: Data Management
        item {
            SectionHeader("DATA MANAGEMENT")
        }

        item {
            CardContainer {
                // Export Data
                SettingActionRow(
                    icon = Icons.Outlined.FileDownload,
                    title = "Export Expense Report",
                    onClick = {
                        val report = viewModel.exportDataSummary()
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("SpendWise Report", report))
                        Toast.makeText(context, "Report copied to clipboard!", Toast.LENGTH_SHORT).show()
                    }
                )

                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

                // Reset Sample Data
                SettingActionRow(
                    icon = Icons.Outlined.RestartAlt,
                    title = "Reset to Sample Data",
                    onClick = { showResetSampleConfirm = true }
                )

                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)

                // Clear All Data
                SettingActionRow(
                    icon = Icons.Outlined.DeleteForever,
                    title = "Clear All Transactions",
                    textColor = ErrorRed,
                    onClick = { showClearDataConfirm = true }
                )
            }
        }

        // Section: About
        item {
            SectionHeader("ABOUT")
        }

        item {
            CardContainer {
                SettingRow(icon = Icons.Outlined.Info, title = "Application", detail = "SpendWise v1.0")
                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)
                SettingRow(icon = Icons.Outlined.School, title = "Course Project", detail = "MCA MAD Activity 1")
                HorizontalDivider(color = CardBorder, thickness = 0.8.dp)
                SettingRow(icon = Icons.Outlined.Layers, title = "Architecture", detail = "Jetpack Compose + Room")
            }
        }
    }

    // Profile Edit Dialog
    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            shape = RoundedCornerShape(22.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text("Edit Student Profile", fontFamily = SerifFamily, fontSize = 20.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Student Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editId,
                        onValueChange = { editId = it },
                        label = { Text("College / Roll ID") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank()) {
                            viewModel.updateProfile(editName, editId)
                            showProfileDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkPine)
                ) {
                    Text("Save", fontFamily = SansFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text("Cancel", fontFamily = SansFamily, color = TextSecondary)
                }
            }
        )
    }

    // Reset Confirmation
    if (showResetSampleConfirm) {
        ConfirmationDialog(
            title = "Reset to Sample Data?",
            message = "This will restore initial student income and expense samples for testing and evaluation.",
            confirmText = "Reset",
            onConfirm = {
                showResetSampleConfirm = false
                viewModel.resetToSampleData {
                    Toast.makeText(context, "Sample data restored!", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = { showResetSampleConfirm = false }
        )
    }

    // Clear Confirmation
    if (showClearDataConfirm) {
        ConfirmationDialog(
            title = "Clear All Data?",
            message = "This will permanently remove all transactions and budgets. Are you sure?",
            confirmText = "Clear All",
            onConfirm = {
                showClearDataConfirm = false
                viewModel.clearAllData {
                    Toast.makeText(context, "All data cleared.", Toast.LENGTH_SHORT).show()
                }
            },
            onDismiss = { showClearDataConfirm = false }
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontFamily = SansFamily,
        fontSize = 9.5.sp,
        letterSpacing = 1.8.sp,
        color = TextSecondary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
    )
}

@Composable
private fun CardContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
        content = content
    )
}

@Composable
private fun SettingRow(icon: ImageVector, title: String, detail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = DarkPine, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontFamily = SansFamily,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = detail,
            fontFamily = SansFamily,
            fontSize = 13.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun SettingActionRow(
    icon: ImageVector,
    title: String,
    textColor: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (textColor != Color.Unspecified) textColor else DarkPine, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontFamily = SansFamily,
                fontSize = 14.sp,
                color = if (textColor != Color.Unspecified) textColor else MaterialTheme.colorScheme.onSurface
            )
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = TextMuted)
    }
}
