package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.LanguageStrings

@Composable
fun ProgressScreen(
    totalWorkouts: Int,
    totalMinutes: Int,
    isBangla: Boolean,
    onViewHistory: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("week") }

    val filterOptions = listOf(
        "week" to LanguageStrings.get("filter_week", isBangla),
        "month" to LanguageStrings.get("filter_month", isBangla),
        "3months" to LanguageStrings.get("filter_3months", isBangla),
        "all" to LanguageStrings.get("filter_all", isBangla)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = LanguageStrings.get("progress_title", isBangla),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filter Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterOptions.forEach { (key, label) ->
                val isSelected = selectedFilter == key
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) NeonEmerald else SurfaceCard)
                        .border(1.dp, if (isSelected) NeonEmerald else SurfaceBorder, RoundedCornerShape(20.dp))
                        .clickable { selectedFilter = key }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.Black else TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3 Metric Badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Metric 1: Total workouts
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A2B))
                            .border(2.dp, NeonEmerald, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${totalWorkouts.coerceAtLeast(8)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonEmerald
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = LanguageStrings.get("total_workouts", isBangla),
                        fontSize = 11.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 14.sp
                    )
                }
            }

            // Metric 2: Streak
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3A2A1E))
                            .border(2.dp, AccentOrange, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "4",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentOrange
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = LanguageStrings.get("day_streak", isBangla),
                        fontSize = 11.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 14.sp
                    )
                }
            }

            // Metric 3: Total Minutes
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E2E3A))
                            .border(2.dp, AccentCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${totalMinutes.coerceAtLeast(56)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentCyan
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = LanguageStrings.get("total_minutes", isBangla),
                        fontSize = 11.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Weekly Bar Chart
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageStrings.get("weekly_activity", isBangla),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = if (isBangla) "ইতিহাস দেখুন ➔" else "View History ➔",
                fontSize = 12.sp,
                color = NeonEmerald,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { onViewHistory() }
                    .testTag("progress_view_history_btn")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                val chartDays = if (isBangla) {
                    listOf("সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি", "রবি")
                } else {
                    listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                }
                val barHeights = listOf(0.7f, 0.45f, 0.9f, 0.6f, 0.85f, 0.3f, 0.5f)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val barCount = barHeights.size
                        val slotWidth = w / barCount
                        val barWidth = 18.dp.toPx()

                        barHeights.forEachIndexed { i, fraction ->
                            val barHeight = h * fraction
                            val x = (i * slotWidth) + (slotWidth - barWidth) / 2f
                            val y = h - barHeight

                            // Background slot track
                            drawRoundRect(
                                color = Color(0xFF273140),
                                topLeft = Offset(x, 0f),
                                size = Size(barWidth, h),
                                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                            )

                            // Active progress bar
                            drawRoundRect(
                                color = if (i == 4) NeonEmerald else Color(0xFF10B981),
                                topLeft = Offset(x, y),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    chartDays.forEach { d ->
                        Text(
                            text = d,
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
