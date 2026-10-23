package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.LanguageStrings

@Composable
fun ReminderScreen(
    initialEnabled: Boolean,
    initialTime: String,
    initialDays: String,
    isBangla: Boolean,
    onSaveReminder: (Boolean, String, String) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var isEnabled by remember { mutableStateOf(initialEnabled) }
    var reminderTime by remember { mutableStateOf(initialTime.ifEmpty { "07:00 AM" }) }

    val allDays = if (isBangla) listOf("সোম", "মঙ্গল", "বুধ", "বৃহ", "শুক্র", "শনি", "রবি")
    else listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val selectedDays = remember {
        mutableStateListOf<String>().apply {
            addAll(allDays)
        }
    }

    var showSavedMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Top App Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SurfaceCard)
                        .testTag("reminders_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = LanguageStrings.get("reminder_settings", isBangla),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Reminder Switch Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = LanguageStrings.get("daily_reminder", isBangla),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isBangla) "প্রতিদিন নির্দিষ্ট সময়ে পুশ নোটিফিকেশন" else "Push notification at scheduled time",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { isEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = NeonEmerald,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = Color(0xFF273140)
                        ),
                        modifier = Modifier.testTag("reminder_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Time Selector Card
            Text(
                text = LanguageStrings.get("select_time", isBangla),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = reminderTime,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonEmerald
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("06:00 AM", "07:00 AM", "06:30 PM", "08:00 PM").forEach { t ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (reminderTime == t) NeonEmerald else Color(0xFF273140))
                                    .clickable { reminderTime = t }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = t.substringBefore(" "),
                                    fontSize = 11.sp,
                                    color = if (reminderTime == t) Color.Black else TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Reminder Days Chips
            Text(
                text = LanguageStrings.get("reminder_days", isBangla),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                allDays.forEach { day ->
                    val isDaySelected = selectedDays.contains(day)
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (isDaySelected) NeonEmerald else Color(0xFF273140))
                            .clickable {
                                if (isDaySelected) selectedDays.remove(day) else selectedDays.add(day)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDaySelected) Color.Black else TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Notification permission info card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Alert",
                        tint = NeonEmerald,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = LanguageStrings.get("notification_permission", isBangla),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = LanguageStrings.get("notification_desc", isBangla),
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            if (showSavedMessage) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = if (isBangla) "✓ রিমাইন্ডার সফলভাবে সংরক্ষিত হয়েছে" else "✓ Reminder saved successfully!",
                    color = NeonEmerald,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        // Save Button
        Button(
            onClick = {
                onSaveReminder(isEnabled, reminderTime, selectedDays.joinToString(","))
                showSavedMessage = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_reminder_btn"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonEmerald,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = LanguageStrings.get("save_btn", isBangla),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
