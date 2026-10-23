package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.SurfaceCard
import kotlin.math.sin

@Composable
fun ExerciseCanvasAnimation(
    animationType: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "exercise_transition")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = if (isPlaying) FastOutSlowInEasing else LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "exercise_cycle"
    )

    val fastProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fast_cycle"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCard),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Background subtle floor mat
            val floorY = h * 0.78f
            drawLine(
                color = Color(0xFF273140),
                start = Offset(w * 0.1f, floorY),
                end = Offset(w * 0.9f, floorY),
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round
            )

            val headRadius = 16.dp.toPx()
            val limbStroke = Stroke(width = 9.dp.toPx(), cap = StrokeCap.Round)
            val glowColor = NeonEmerald
            val bodyColor = Color.White
            val accentColor = AccentCyan

            when (animationType) {
                "push_up" -> {
                    // Head & body moves down and up
                    val dip = progress * (h * 0.16f)
                    val headX = w * 0.28f
                    val headY = (h * 0.52f) + dip
                    val shoulderX = w * 0.38f
                    val shoulderY = (h * 0.54f) + dip
                    val hipX = w * 0.62f
                    val hipY = (h * 0.62f) + (dip * 0.5f)
                    val feetX = w * 0.82f
                    val feetY = floorY - 6.dp.toPx()

                    // Hands on floor
                    val handX = w * 0.38f
                    val handY = floorY - 4.dp.toPx()
                    val elbowX = w * 0.34f - (dip * 0.3f)
                    val elbowY = shoulderY + (handY - shoulderY) * 0.5f + (dip * 0.4f)

                    // Draw body line (spine & legs)
                    drawLine(color = glowColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 10.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(feetX, feetY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Draw arm (shoulder -> elbow -> hand)
                    drawLine(color = accentColor, start = Offset(shoulderX, shoulderY), end = Offset(elbowX, elbowY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = Offset(elbowX, elbowY), end = Offset(handX, handY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)

                    // Head
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                "squat" -> {
                    // Standing and squatting
                    val squatDepth = progress * (h * 0.2f)
                    val headX = w * 0.5f
                    val headY = (h * 0.24f) + squatDepth
                    val shoulderX = w * 0.5f
                    val shoulderY = (h * 0.34f) + squatDepth
                    val hipX = (w * 0.52f) + (squatDepth * 0.3f)
                    val hipY = (h * 0.50f) + squatDepth

                    val kneeX = (w * 0.46f) - (squatDepth * 0.2f)
                    val kneeY = (h * 0.64f) + (squatDepth * 0.2f)

                    val footX = w * 0.48f
                    val footY = floorY - 4.dp.toPx()

                    // Arms extended forward for balance
                    val handX = (w * 0.30f) - (progress * 20.dp.toPx())
                    val handY = shoulderY + (progress * 10.dp.toPx())

                    // Torso
                    drawLine(color = glowColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 10.dp.toPx(), cap = StrokeCap.Round)
                    // Legs (hip -> knee -> foot)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Arm
                    drawLine(color = accentColor, start = Offset(shoulderX, shoulderY), end = Offset(handX, handY), strokeWidth = 7.dp.toPx(), cap = StrokeCap.Round)

                    // Head
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                "plank" -> {
                    // Static straight plank with pulsating core energy
                    val headX = w * 0.26f
                    val headY = h * 0.56f
                    val shoulderX = w * 0.36f
                    val shoulderY = h * 0.58f
                    val hipX = w * 0.60f
                    val hipY = h * 0.62f
                    val feetX = w * 0.82f
                    val feetY = floorY - 4.dp.toPx()

                    val elbowX = w * 0.36f
                    val elbowY = floorY - 4.dp.toPx()

                    // Core glow aura
                    val pulseRadius = 24.dp.toPx() + (progress * 10.dp.toPx())
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(Color(0x6600E676), Color.Transparent),
                            center = Offset(hipX, hipY),
                            radius = pulseRadius
                        ),
                        radius = pulseRadius,
                        center = Offset(hipX, hipY)
                    )

                    // Spine & legs
                    drawLine(color = glowColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 10.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(feetX, feetY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Arm
                    drawLine(color = accentColor, start = Offset(shoulderX, shoulderY), end = Offset(elbowX, elbowY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = Offset(elbowX, elbowY), end = Offset(elbowX - 25.dp.toPx(), elbowY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)

                    // Head
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                "mountain_climber" -> {
                    val headX = w * 0.28f
                    val headY = h * 0.50f
                    val shoulderX = w * 0.38f
                    val shoulderY = h * 0.54f
                    val hipX = w * 0.60f
                    val hipY = h * 0.56f
                    val backFootX = w * 0.82f
                    val backFootY = floorY - 4.dp.toPx()

                    // Knee driving forward
                    val kneeDrive = fastProgress * (w * 0.20f)
                    val drivingKneeX = hipX - (w * 0.12f) - kneeDrive
                    val drivingKneeY = h * 0.66f

                    // Hands
                    val handX = w * 0.38f
                    val handY = floorY - 4.dp.toPx()

                    // Torso
                    drawLine(color = glowColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 10.dp.toPx(), cap = StrokeCap.Round)

                    // Arms
                    drawLine(color = accentColor, start = Offset(shoulderX, shoulderY), end = Offset(handX, handY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)

                    // Driving leg
                    drawLine(color = NeonEmerald, start = Offset(hipX, hipY), end = Offset(drivingKneeX, drivingKneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Ground leg
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(backFootX, backFootY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Head
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                "jumping_jacks" -> {
                    val jack = progress
                    val headX = w * 0.5f
                    val headY = h * 0.26f
                    val torsoTop = h * 0.36f
                    val torsoBottom = h * 0.56f

                    // Hands go up and down
                    val handSpreadX = (w * 0.22f) + (jack * (w * 0.18f))
                    val handY = (h * 0.44f) - (jack * (h * 0.28f))

                    // Feet spread outward
                    val footSpreadX = (w * 0.08f) + (jack * (w * 0.18f))
                    val footY = floorY - 4.dp.toPx()

                    // Torso
                    drawLine(color = glowColor, start = Offset(headX, torsoTop), end = Offset(headX, torsoBottom), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)

                    // Left & Right Arms
                    drawLine(color = accentColor, start = Offset(headX, torsoTop), end = Offset(headX - handSpreadX, handY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = Offset(headX, torsoTop), end = Offset(headX + handSpreadX, handY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)

                    // Left & Right Legs
                    drawLine(color = bodyColor, start = Offset(headX, torsoBottom), end = Offset(headX - footSpreadX, footY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(headX, torsoBottom), end = Offset(headX + footSpreadX, footY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Head
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                else -> {
                    // Default athletic stretch / pose
                    val arch = progress * 15.dp.toPx()
                    val headX = w * 0.32f
                    val headY = h * 0.42f - arch
                    val shoulderX = w * 0.42f
                    val shoulderY = h * 0.52f - (arch * 0.5f)
                    val hipX = w * 0.65f
                    val hipY = h * 0.70f

                    drawLine(color = glowColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 10.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = accentColor, start = Offset(shoulderX, shoulderY), end = Offset(w * 0.42f, floorY - 4.dp.toPx()), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(w * 0.85f, floorY - 4.dp.toPx()), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }
            }
        }
    }
}
