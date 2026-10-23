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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExerciseEntity
import com.example.ui.components.ExerciseCanvasAnimation
import com.example.ui.components.TimerRing
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.LanguageStrings

@Composable
fun ActiveWorkoutScreen(
    currentExercise: ExerciseEntity?,
    nextExercise: ExerciseEntity?,
    currentIndex: Int,
    totalExercises: Int,
    timerSeconds: Int,
    totalSeconds: Int,
    isPaused: Boolean,
    isBangla: Boolean,
    onPauseToggle: () -> Unit,
    onSkip: () -> Unit,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    if (currentExercise == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark))
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
                    .testTag("active_workout_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isBangla) currentExercise.titleBn else currentExercise.titleEn,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${currentIndex + 1} / $totalExercises",
                    fontSize = 12.sp,
                    color = NeonEmerald,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(modifier = Modifier.size(40.dp))
        }

        // Animated Exercise Guide View
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
        ) {
            ExerciseCanvasAnimation(
                animationType = currentExercise.animationType,
                modifier = Modifier.fillMaxSize(),
                isPlaying = !isPaused
            )
        }

        // Progress bar for current exercise
        val progress = if (totalSeconds > 0) 1f - (timerSeconds.toFloat() / totalSeconds.toFloat()) else 0f
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = NeonEmerald,
            trackColor = Color(0xFF273140)
        )

        // Glowing Circular Countdown Timer
        TimerRing(
            secondsRemaining = timerSeconds,
            totalSeconds = totalSeconds,
            subtitle = LanguageStrings.get("seconds", isBangla),
            ringSize = 180.dp,
            strokeWidth = 12.dp
        )

        // Next Exercise Preview Card
        if (nextExercise != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExerciseCanvasAnimation(
                        animationType = nextExercise.animationType,
                        modifier = Modifier.size(48.dp),
                        isPlaying = false
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${if (isBangla) "পরবর্তী: " else "Next: "}${if (isBangla) nextExercise.titleBn else nextExercise.titleEn}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${nextExercise.durationSec} ${LanguageStrings.get("seconds", isBangla)}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // Action Controls (Pause / Skip / Finish)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPauseToggle,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("active_workout_pause_btn"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00B0FF),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = "Pause",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPaused) LanguageStrings.get("resume", isBangla) else LanguageStrings.get("pause", isBangla),
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onSkip,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("active_workout_skip_btn"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceCard,
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Skip",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = LanguageStrings.get("skip", isBangla),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("active_workout_finish_btn"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentRed.copy(alpha = 0.85f),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = LanguageStrings.get("finish", isBangla),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
