package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ExerciseEntity
import com.example.ui.components.ExerciseCanvasAnimation
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.SkyBlueDark
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.LanguageStrings
import kotlinx.coroutines.delay

@Composable
fun ExerciseVideoPlayerScreen(
    exercise: ExerciseEntity?,
    isBangla: Boolean,
    onNextExercise: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    if (exercise == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark))
        return
    }

    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0.35f) }
    var is3DPhotoRenderMode by remember { mutableStateOf(true) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }

    val totalDuration = exercise.durationSec.coerceAtLeast(30)
    val elapsedSeconds = (currentProgress * totalDuration).toInt()

    // Smooth video playback loop ticker
    LaunchedEffect(isPlaying, playbackSpeed) {
        while (isPlaying) {
            delay((100 / playbackSpeed).toLong())
            currentProgress = if (currentProgress >= 1f) 0f else currentProgress + (0.1f / totalDuration)
        }
    }

    // Glowing 3D muscle holographic pulse
    val infiniteTransition = rememberInfiniteTransition(label = "video_hud_pulse")
    val hudPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hud_pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Header
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
                    .testTag("video_player_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isBangla) exercise.titleBn else exercise.titleEn,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (isBangla) "৩ডি অ্যানিমেশন ভিডিও প্লেয়ার" else "3D Animated Video Guide",
                    fontSize = 11.sp,
                    color = SkyBlueLight,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(
                onClick = { is3DPhotoRenderMode = !is3DPhotoRenderMode },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0C4A6E))
            ) {
                Icon(
                    imageVector = Icons.Default.ViewInAr,
                    contentDescription = "Toggle 3D View",
                    tint = SkyBlueLight,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 3D Video Player Frame
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, SkyBlueLight.copy(alpha = 0.7f), RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (is3DPhotoRenderMode) {
                    val imageRes = if (exercise.animationType == "kegel_glute_bridge" || exercise.animationType == "kegel_butterfly") {
                        R.drawable.img_kegel_bridge_3d_1792773563176
                    } else {
                        R.drawable.img_kegel_3d_render_1792773545699
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "3D Render",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Holographic scanline overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x3300B4D8 * hudPulse.toInt()),
                                        Color.Transparent,
                                        Color(0xAA0A0F1D)
                                    )
                                )
                            )
                    )
                } else {
                    ExerciseCanvasAnimation(
                        animationType = exercise.animationType,
                        modifier = Modifier.fillMaxSize(),
                        isPlaying = isPlaying
                    )
                }

                // Top Mode Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xDD0C4A6E))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (is3DPhotoRenderMode) "3D BIOMECHANICAL RENDER" else "3D KINETIC SIMULATION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyBlueLight
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xAA000000))
                            .clickable {
                                playbackSpeed = when (playbackSpeed) {
                                    1.0f -> 1.25f
                                    1.25f -> 1.5f
                                    1.5f -> 0.75f
                                    else -> 1.0f
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${playbackSpeed}x SPEED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Squeeze & Lift indicator
                val isSqueezing = (currentProgress * 10).toInt() % 2 == 0
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSqueezing) SkyBlueLight else Color(0x9918233C))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isSqueezing) "● সংকোচন (Squeeze & Lift)" else "○ শিথিল (Relax & Breathe)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSqueezing) Color.Black else SkyBlueLight
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Video Progress Scrubber Bar & Timestamps
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val minElapsed = elapsedSeconds / 60
            val secElapsed = elapsedSeconds % 60
            val minTotal = totalDuration / 60
            val secTotal = totalDuration % 60

            Text(
                text = String.format("%02d:%02d / %02d:%02d", minElapsed, secElapsed, minTotal, secTotal),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SkyBlueLight
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (is3DPhotoRenderMode) "ক্যানভাস ৩ডি মোড ➔" else "বায়োমেডিক্যাল ৩ডি ➔",
                    fontSize = 11.sp,
                    color = AccentCyan,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { is3DPhotoRenderMode = !is3DPhotoRenderMode }
                )
            }
        }

        Slider(
            value = currentProgress,
            onValueChange = { currentProgress = it },
            colors = SliderDefaults.colors(
                thumbColor = SkyBlueLight,
                activeTrackColor = SkyBlueLight,
                inactiveTrackColor = Color(0xFF243452)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Video Controls Row (Rewind, Play/Pause, Forward)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { currentProgress = (currentProgress - 0.2f).coerceAtLeast(0f) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
            ) {
                Icon(imageVector = Icons.Default.FastRewind, contentDescription = "Rewind", tint = TextPrimary)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(SkyBlueLight)
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            IconButton(
                onClick = { currentProgress = (currentProgress + 0.2f).coerceAtMost(1f) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
            ) {
                Icon(imageVector = Icons.Default.FastForward, contentDescription = "Forward", tint = TextPrimary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Exercise Target & Rest details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = LanguageStrings.get("target", isBangla),
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isBangla) exercise.targetBn else exercise.targetEn,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = LanguageStrings.get("rest", isBangla),
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${exercise.restSec} ${LanguageStrings.get("seconds", isBangla)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyBlueLight
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Instructions Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SurfaceBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBangla) "৩ডি অ্যানাটমিক্যাল নির্দেশাবলী" else "3D Anatomical Instructions",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SkyBlueLight
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isBangla) exercise.instructionsBn else exercise.instructionsEn,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Next Exercise Button
        Button(
            onClick = onNextExercise,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("video_next_exercise_btn"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlueLight,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "${LanguageStrings.get("next_exercise", isBangla)} ➔",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
