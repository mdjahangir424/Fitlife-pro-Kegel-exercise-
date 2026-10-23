package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.util.LanguageStrings

@Composable
fun OnboardingScreen(
    isBangla: Boolean,
    onStart: () -> Unit,
    onSkip: () -> Unit
) {
    var currentSlide by remember { mutableIntStateOf(0) }

    val titles = if (isBangla) listOf(
        "আপনার ফিটনেস\nযাত্রা শুরু হোক আজই",
        "অ্যানিমেটেড গাইড দেখে\nব্যায়াম শিখুন সহজে",
        "প্রতিদিনের ওয়ার্কআউট\nট্র্যাক ও রেকর্ড করুন",
        "সম্পূর্ণ অফলাইন ও\nনিরাপদ ডেটাবেস"
    ) else listOf(
        "Start Your Fitness\nJourney Today",
        "Learn Workouts With\nAnimated Visual Guides",
        "Track Daily Streaks &\nWorkout Milestones",
        "100% Offline Database\nUnder Your Control"
    )

    val descriptions = if (isBangla) listOf(
        "ভিডিও দেখে ব্যায়াম করুন, নিজেকে আরও শক্তিশালী ও সুস্থ রাখুন।",
        "সহজ ও নিখুঁত ভঙ্গিমায় প্রতিটি ব্যায়াম সম্পন্ন করতে সাহায্য করে।",
        "ক্যালোরি, ব্যায়ামের সময় এবং ব্যক্তিগত লক্ষ্য অর্জন করুন।",
        "আপনার সম্পূর্ণ তথ্য ডিভাইসেই সংরক্ষিত থাকে, কোন তথ্য চুরি হয় না।"
    ) else listOf(
        "Exercise with animated guidance, stay healthy and achieve your goals.",
        "Interactive animated workout guides help you maintain perfect form.",
        "Monitor your calories, active minutes, and consistency streaks.",
        "Your fitness data stays locally on your phone under your 100% control."
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Hero visual
        Image(
            painter = painterResource(id = R.drawable.fitlife_workout_hero_1792717888791),
            contentDescription = "Workout Hero",
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for smooth contrast
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(390.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x990D1117),
                            BackgroundDark
                        )
                    )
                )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titles[currentSlide],
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = descriptions[currentSlide],
                fontSize = 15.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 4 dots indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0..3) {
                    Box(
                        modifier = Modifier
                            .size(if (i == currentSlide) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (i == currentSlide) NeonEmerald else Color(0xFF30363D))
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (currentSlide < 3) {
                        currentSlide++
                    } else {
                        onStart()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("onboarding_start_button"),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonEmerald,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = if (currentSlide == 3) LanguageStrings.get("start_now", isBangla) else LanguageStrings.get("continue_btn", isBangla),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onSkip,
                modifier = Modifier.testTag("onboarding_skip_button")
            ) {
                Text(
                    text = LanguageStrings.get("skip", isBangla),
                    fontSize = 14.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
