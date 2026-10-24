package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkoutHistoryEntity
import com.example.ui.components.CategoryDistributionDonutChart
import com.example.ui.components.ChartAnalyticsEngine
import com.example.ui.components.DailyStreakTrackerCard
import com.example.ui.components.InteractiveActivityBarChart
import com.example.ui.components.LongTermProgressSplineChart
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.GoldenTrophy
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.LanguageStrings

@Composable
fun ProgressScreen(
    totalWorkouts: Int,
    totalMinutes: Int,
    workoutHistory: List<WorkoutHistoryEntity> = emptyList(),
    isBangla: Boolean,
    onViewHistory: () -> Unit
) {
    var selectedTimeframe by remember { mutableStateOf("week") }
    var selectedMetric by remember { mutableStateOf("minutes") }

    val streakAnalytics = remember(workoutHistory) {
        ChartAnalyticsEngine.computeStreakAnalytics(workoutHistory)
    }

    val activityPoints = remember(workoutHistory, selectedTimeframe, isBangla) {
        ChartAnalyticsEngine.computeActivityPoints(workoutHistory, selectedTimeframe, isBangla)
    }

    val categories = remember(isBangla) {
        ChartAnalyticsEngine.computeCategoryDistribution(isBangla)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("progress_dashboard_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Header Row: Dashboard Title & History Shortcut
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = LanguageStrings.get("progress_title", isBangla),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = if (isBangla) "পেলভিক ধারাবাহিকতা ও কার্যকলাপ ড্যাশবোর্ড" else "Pelvic Consistency & Activity Analytics",
                    fontSize = 12.sp,
                    color = TextMuted,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0C4A6E))
                    .border(1.dp, SkyBlueLight.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .clickable { onViewHistory() }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("progress_view_history_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = SkyBlueLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBangla) "ইতিহাস" else "History",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyBlueLight
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // KPI Badges Row (4 Stats)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Stat 1: Current Streak
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = AccentOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${streakAnalytics.currentStreak}d",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentOrange
                    )
                    Text(
                        text = LanguageStrings.get("day_streak", isBangla),
                        fontSize = 9.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            // Stat 2: Total Sessions
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = "Sessions",
                        tint = SkyBlueLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${totalWorkouts.coerceAtLeast(streakAnalytics.totalWorkouts)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = SkyBlueLight
                    )
                    Text(
                        text = LanguageStrings.get("total_workouts", isBangla),
                        fontSize = 9.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            // Stat 3: Total Minutes Active
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${totalMinutes.coerceAtLeast(streakAnalytics.totalMinutes)}m",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentCyan
                    )
                    Text(
                        text = LanguageStrings.get("total_minutes", isBangla),
                        fontSize = 9.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }

            // Stat 4: Consistency Rate
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "Consistency",
                        tint = GoldenTrophy,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(streakAnalytics.completionRate * 100).toInt()}%",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldenTrophy
                    )
                    Text(
                        text = LanguageStrings.get("consistency_rate", isBangla),
                        fontSize = 9.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 1. Daily Exercise Streak Chart Card
        DailyStreakTrackerCard(
            streakAnalytics = streakAnalytics,
            isBangla = isBangla
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Interactive Activity Bar Chart (Touch / Scrubbing enabled)
        InteractiveActivityBarChart(
            points = activityPoints,
            selectedTimeframe = selectedTimeframe,
            onTimeframeSelected = { selectedTimeframe = it },
            selectedMetric = selectedMetric,
            onMetricSelected = { selectedMetric = it },
            isBangla = isBangla
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Long-Term Progress Spline / Trend Curve
        LongTermProgressSplineChart(
            points = activityPoints,
            selectedMetric = selectedMetric,
            isBangla = isBangla
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 4. Workout Category Breakdown Donut Chart
        CategoryDistributionDonutChart(
            categories = categories,
            isBangla = isBangla
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Consistency Motivation Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SurfaceBorder, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0C4A6E))
                        .border(1.5.dp, SkyBlueLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Badge",
                        tint = GoldenTrophy,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = if (isBangla) "পেলভিক পাওয়ার ধারাবাহিকতা" else "Pelvic Power Consistency",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isBangla)
                            "প্রতিদিনের ক্যাগেল রুটিন সম্পন্ন করে আপনার স্ট্রিক অক্ষুণ্ণ রাখুন এবং পেলভিক শক্তি বৃদ্ধি করুন।"
                        else
                            "Keep your daily streak active by finishing your routine each day to build core stability.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
