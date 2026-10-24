package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfileEntity
import com.example.data.model.WorkoutEntity
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.SkyBlueDark
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.appBackgroundColor
import com.example.ui.theme.appBorderColor
import com.example.ui.theme.appSurfaceColor
import com.example.ui.theme.appTextMuted
import com.example.ui.theme.appTextPrimary
import com.example.ui.theme.appTextSecondary
import com.example.ui.theme.isDarkTheme
import com.example.util.LanguageStrings

@Composable
fun HomeScreen(
    userProfile: UserProfileEntity?,
    workouts: List<WorkoutEntity>,
    isBangla: Boolean,
    themeMode: com.example.ui.theme.AppThemeMode,
    onWorkoutClick: (WorkoutEntity) -> Unit,
    onStartWorkout: (WorkoutEntity) -> Unit,
    onNotificationClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onToggleTheme: () -> Unit
) {
    val isDark = isDarkTheme()
    val bgColor = appBackgroundColor()
    val surfaceColor = appSurfaceColor()
    val borderColor = appBorderColor()
    val textPrimaryColor = appTextPrimary()
    val textMutedColor = appTextMuted()
    val textSecondaryColor = appTextSecondary()

    val userName = userProfile?.name ?: if (isBangla) "জাহাঙ্গীর" else "Jahangir"
    val todayWorkout = workouts.firstOrNull { it.id == "wb_kegel_main" } ?: workouts.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.fitlife_skyblue_icon_1792774391534),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "FitLife Pro",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textPrimaryColor
                    )
                    Text(
                        text = LanguageStrings.get("tagline", isBangla),
                        fontSize = 9.sp,
                        color = SkyBlueLight
                    )
                }
            }

            // Quick Actions (Theme Switcher & Notification)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Theme Mode Switcher Button (Dark / Light / System)
                IconButton(
                    onClick = onToggleTheme,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(surfaceColor)
                        .border(1.dp, borderColor, CircleShape)
                        .testTag("home_theme_toggle_button")
                ) {
                    Text(
                        text = when (themeMode) {
                            com.example.ui.theme.AppThemeMode.DARK -> "🌙"
                            com.example.ui.theme.AppThemeMode.LIGHT -> "☀️"
                            com.example.ui.theme.AppThemeMode.SYSTEM -> "⚙️"
                        },
                        fontSize = 15.sp
                    )
                }

                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(surfaceColor)
                        .border(1.dp, borderColor, CircleShape)
                        .testTag("home_notification_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = textPrimaryColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Greeting
        Text(
            text = "${LanguageStrings.get("good_morning", isBangla)}, $userName!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimaryColor
        )
        Text(
            text = LanguageStrings.get("fitness_quote", isBangla),
            fontSize = 13.sp,
            color = textMutedColor,
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Today's Featured Kegel Workout Card
        if (todayWorkout != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, SkyBlueLight.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .clickable { onWorkoutClick(todayWorkout) }
                    .testTag("today_workout_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.fitlife_workout_hero_1792717888791),
                        contentDescription = "Kegel Workout Hero",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient Scrim with sky blue undertone
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFA0A0F1D),
                                        Color(0xD00A0F1D),
                                        Color(0x400A0F1D)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF0C4A6E))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = LanguageStrings.get("today_workout", isBangla),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SkyBlueLight
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (isBangla) todayWorkout.titleBn else todayWorkout.titleEn,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "${todayWorkout.exerciseCount} ${LanguageStrings.get("exercises", isBangla)} • ${todayWorkout.durationMinutes} ${LanguageStrings.get("minutes", isBangla)}",
                                fontSize = 12.sp,
                                color = Color(0xFFBAE6FD),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Button(
                            onClick = { onStartWorkout(todayWorkout) },
                            modifier = Modifier
                                .height(40.dp)
                                .testTag("start_workout_home_btn"),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SkyBlueLight,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = LanguageStrings.get("start", isBangla),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Today's Progress Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageStrings.get("today_progress", isBangla),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3 Stat Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card 1: Exercises Done
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = "Kegels",
                        tint = SkyBlueLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "10/10", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimaryColor)
                    Text(
                        text = LanguageStrings.get("exercises_done", isBangla),
                        fontSize = 10.sp,
                        color = textMutedColor,
                        maxLines = 1
                    )
                }
            }

            // Card 2: Minutes
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time",
                        tint = SkyBluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "20", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimaryColor)
                    Text(
                        text = LanguageStrings.get("time_min", isBangla),
                        fontSize = 10.sp,
                        color = textMutedColor,
                        maxLines = 1
                    )
                }
            }

            // Card 3: Calories
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Calories",
                        tint = AccentOrange,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "110", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimaryColor)
                    Text(
                        text = LanguageStrings.get("calories_burned", isBangla),
                        fontSize = 10.sp,
                        color = textMutedColor,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Weekly Activity Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = LanguageStrings.get("weekly_activity", isBangla),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimaryColor
            )
            Text(
                text = if (isBangla) "ক্যালেন্ডার দেখুন ➔" else "View Calendar ➔",
                fontSize = 12.sp,
                color = SkyBlueLight,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onCalendarClick() }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekday dots card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                .clickable { onCalendarClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = surfaceColor)
        ) {
            val days = if (isBangla) {
                listOf("সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি", "রবি")
            } else {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            }
            val activeState = listOf(true, true, true, true, false, false, false)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                days.forEachIndexed { idx, day ->
                    val isActive = activeState[idx]
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day,
                            fontSize = 11.sp,
                            color = if (isActive) (if (isDark) textPrimaryColor else SkyBlueDark) else textMutedColor,
                            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isActive) SkyBlueLight else if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isActive) {
                                Text(text = "✓", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
