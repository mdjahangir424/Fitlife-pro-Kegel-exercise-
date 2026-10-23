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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.SkyBlueDark
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SurfaceCard

@Composable
fun ExerciseCanvasAnimation(
    animationType: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "kegel_3d_transition")

    // Smooth breathing & contraction cycle (3.5 seconds)
    val cycleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = if (isPlaying) FastOutSlowInEasing else LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "kegel_cycle"
    )

    // Fast flick cycle (1.0 second)
    val fastCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fast_flick_cycle"
    )

    val isSqueezing = cycleProgress > 0.45f

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F1B2E),
                        Color(0xFF14243B),
                        Color(0xFF0D1726)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 3D Isometric floor grid plane
            val floorY = h * 0.76f
            val gridColor = Color(0x1F38BDF8)
            for (i in -3..3) {
                drawLine(
                    color = gridColor,
                    start = Offset(w * 0.5f + (i * w * 0.14f), floorY - (h * 0.12f)),
                    end = Offset(w * 0.5f + (i * w * 0.22f), floorY + (h * 0.16f)),
                    strokeWidth = 1.5.dp.toPx()
                )
            }
            // Horizontal depth perspective lines
            drawLine(color = gridColor, start = Offset(w * 0.1f, floorY - (h * 0.08f)), end = Offset(w * 0.9f, floorY - (h * 0.08f)), strokeWidth = 1.dp.toPx())
            drawLine(color = gridColor, start = Offset(w * 0.05f, floorY), end = Offset(w * 0.95f, floorY), strokeWidth = 2.dp.toPx())
            drawLine(color = gridColor, start = Offset(0f, floorY + (h * 0.08f)), end = Offset(w, floorY + (h * 0.08f)), strokeWidth = 2.5.dp.toPx())

            // 3D Pelvic Glow parameters
            val headRadius = 14.dp.toPx()
            val limbStroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            val spineStroke = Stroke(width = 11.dp.toPx(), cap = StrokeCap.Round)
            val skyColor = SkyBlueLight
            val bodyColor = Color(0xFFF1F5F9)
            val depthShade = Color(0xFF94A3B8)

            when (animationType) {
                // 1. Basic Kegel Contract & Hold
                "kegel_basic" -> {
                    val headX = w * 0.22f
                    val headY = floorY - (h * 0.08f)
                    val shoulderX = w * 0.35f
                    val shoulderY = floorY - (h * 0.06f)
                    val hipX = w * 0.56f
                    val hipY = floorY - (h * 0.06f)
                    val kneeX = w * 0.72f
                    val kneeY = floorY - (h * 0.26f)
                    val footX = w * 0.82f
                    val footY = floorY - 2.dp.toPx()

                    // Pelvic floor muscle squeeze glow in 3D
                    val squeezePulse = 18.dp.toPx() + (cycleProgress * 14.dp.toPx())
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(SkyBlueLight.copy(alpha = 0.85f), AccentCyan.copy(alpha = 0.3f), Color.Transparent),
                            center = Offset(hipX, hipY - 6.dp.toPx()),
                            radius = squeezePulse
                        ),
                        radius = squeezePulse,
                        center = Offset(hipX, hipY - 6.dp.toPx())
                    )

                    // Body and bent legs
                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = depthShade, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Arms resting along torso
                    drawLine(color = depthShade, start = Offset(shoulderX, shoulderY), end = Offset(w * 0.50f, floorY), strokeWidth = 6.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 2. Quick Flick Kegels
                "kegel_quick_flicks" -> {
                    val headX = w * 0.22f
                    val headY = floorY - (h * 0.08f)
                    val shoulderX = w * 0.35f
                    val shoulderY = floorY - (h * 0.06f)
                    val hipX = w * 0.56f
                    val hipY = floorY - (h * 0.06f)
                    val kneeX = w * 0.72f
                    val kneeY = floorY - (h * 0.26f)
                    val footX = w * 0.82f
                    val footY = floorY - 2.dp.toPx()

                    // Rapid pulsating shockwave rings
                    val wave = fastCycle * 28.dp.toPx()
                    drawCircle(
                        color = SkyBlueLight.copy(alpha = 1f - fastCycle),
                        radius = wave + 10.dp.toPx(),
                        center = Offset(hipX, hipY - 6.dp.toPx()),
                        style = Stroke(width = 3.dp.toPx())
                    )

                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = depthShade, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 3. Glute Bridge Kegel
                "kegel_glute_bridge" -> {
                    val lift = cycleProgress * (h * 0.22f)
                    val headX = w * 0.20f
                    val headY = floorY - (h * 0.06f)
                    val shoulderX = w * 0.32f
                    val shoulderY = floorY - (h * 0.06f)
                    val hipX = w * 0.54f
                    val hipY = (floorY - (h * 0.08f)) - lift
                    val kneeX = w * 0.72f
                    val kneeY = floorY - (h * 0.26f)
                    val footX = w * 0.80f
                    val footY = floorY - 2.dp.toPx()

                    // Pelvic engagement glow
                    val glowR = 24.dp.toPx() + (cycleProgress * 8.dp.toPx())
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(AccentCyan, SkyBlueDark, Color.Transparent),
                            center = Offset(hipX, hipY),
                            radius = glowR
                        ),
                        radius = glowR,
                        center = Offset(hipX, hipY)
                    )

                    // Bridge line
                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 12.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 10.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = depthShade, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 4. Butterfly Pelvic Stretch
                "kegel_butterfly" -> {
                    val headX = w * 0.24f
                    val headY = floorY - (h * 0.08f)
                    val shoulderX = w * 0.36f
                    val shoulderY = floorY - (h * 0.06f)
                    val hipX = w * 0.56f
                    val hipY = floorY - (h * 0.06f)
                    val footX = w * 0.78f
                    val footY = floorY - 2.dp.toPx()

                    // Butterfly wings (knees opening left/right in 3D perspective)
                    val wingSpread = (cycleProgress * 18.dp.toPx())
                    val topKneeX = w * 0.68f
                    val topKneeY = floorY - (h * 0.22f) + wingSpread

                    val bottomKneeX = w * 0.68f
                    val bottomKneeY = floorY - (h * 0.02f) - (wingSpread * 0.4f)

                    // Pelvic deep center breathing
                    val breathR = 16.dp.toPx() + (cycleProgress * 12.dp.toPx())
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(SkyBlueLight.copy(alpha = 0.9f), Color.Transparent),
                            center = Offset(hipX, hipY),
                            radius = breathR
                        ),
                        radius = breathR,
                        center = Offset(hipX, hipY)
                    )

                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(topKneeX, topKneeY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(topKneeX, topKneeY), end = Offset(footX, footY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)

                    drawLine(color = depthShade, start = Offset(hipX, hipY), end = Offset(bottomKneeX, bottomKneeY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = depthShade, start = Offset(bottomKneeX, bottomKneeY), end = Offset(footX, footY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)

                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 5. Cat-Cow Pelvic Movement
                "kegel_cat_cow" -> {
                    val arch = (cycleProgress - 0.5f) * (h * 0.14f)
                    val headX = w * 0.22f
                    val headY = floorY - (h * 0.32f) + arch
                    val shoulderX = w * 0.36f
                    val shoulderY = floorY - (h * 0.28f)
                    val spineMidX = w * 0.52f
                    val spineMidY = floorY - (h * 0.28f) - arch
                    val hipX = w * 0.68f
                    val hipY = floorY - (h * 0.28f)

                    val handX = w * 0.36f
                    val handY = floorY - 2.dp.toPx()
                    val kneeX = w * 0.68f
                    val kneeY = floorY - 2.dp.toPx()

                    // Pelvic tilt energy aura
                    val tiltPulse = 20.dp.toPx() + (cycleProgress * 10.dp.toPx())
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(SkyBlueLight, Color.Transparent),
                            center = Offset(hipX, hipY),
                            radius = tiltPulse
                        ),
                        radius = tiltPulse,
                        center = Offset(hipX, hipY)
                    )

                    // Curved spine path
                    val spinePath = Path().apply {
                        moveTo(shoulderX, shoulderY)
                        quadraticTo(spineMidX, spineMidY, hipX, hipY)
                    }
                    drawPath(spinePath, color = skyColor, style = Stroke(width = 11.dp.toPx(), cap = StrokeCap.Round))

                    // Limbs to ground
                    drawLine(color = depthShade, start = Offset(shoulderX, shoulderY), end = Offset(handX, handY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 6. Bird Dog Pelvic Brace
                "kegel_bird_dog" -> {
                    val reach = cycleProgress * (w * 0.14f)
                    val headX = w * 0.30f
                    val headY = floorY - (h * 0.28f)
                    val shoulderX = w * 0.40f
                    val shoulderY = floorY - (h * 0.25f)
                    val hipX = w * 0.64f
                    val hipY = floorY - (h * 0.25f)

                    // Extended arm forward
                    val armReachX = w * 0.16f - reach
                    val armReachY = floorY - (h * 0.25f)
                    // Extended leg backward
                    val legReachX = w * 0.84f + reach
                    val legReachY = floorY - (h * 0.25f)

                    // Ground support
                    val supportHandX = w * 0.40f
                    val supportKneeX = w * 0.64f
                    val floorLevel = floorY - 2.dp.toPx()

                    // Core pelvic stabilization glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(AccentCyan, SkyBlueDark, Color.Transparent),
                            center = Offset(w * 0.52f, floorY - (h * 0.25f)),
                            radius = 22.dp.toPx()
                        ),
                        radius = 22.dp.toPx(),
                        center = Offset(w * 0.52f, floorY - (h * 0.25f))
                    )

                    // Torso
                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)
                    // Extended arm & leg
                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(armReachX, armReachY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = skyColor, start = Offset(hipX, hipY), end = Offset(legReachX, legReachY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Support limbs
                    drawLine(color = depthShade, start = Offset(shoulderX, shoulderY), end = Offset(supportHandX, floorLevel), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = depthShade, start = Offset(hipX, hipY), end = Offset(supportKneeX, floorLevel), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 7. Deep Pelvic Squat / Malasana
                "kegel_deep_squat" -> {
                    val squatDepth = cycleProgress * (h * 0.12f)
                    val headX = w * 0.50f
                    val headY = (floorY - (h * 0.38f)) + squatDepth
                    val shoulderX = w * 0.50f
                    val shoulderY = (floorY - (h * 0.26f)) + squatDepth
                    val hipX = w * 0.50f
                    val hipY = (floorY - (h * 0.10f)) + (squatDepth * 0.6f)

                    // Left & Right wide knees
                    val leftKneeX = w * 0.32f
                    val rightKneeX = w * 0.68f
                    val kneeY = floorY - (h * 0.12f)

                    val leftFootX = w * 0.36f
                    val rightFootX = w * 0.64f
                    val feetY = floorY - 2.dp.toPx()

                    // Pelvic floor diaphragm expansion & squeeze
                    val diaphragmR = 24.dp.toPx() + (cycleProgress * 12.dp.toPx())
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(SkyBlueLight.copy(alpha = 0.9f), Color.Transparent),
                            center = Offset(hipX, hipY),
                            radius = diaphragmR
                        ),
                        radius = diaphragmR,
                        center = Offset(hipX, hipY)
                    )

                    // Torso
                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)

                    // Wide thighs & shins
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(leftKneeX, kneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(leftKneeX, kneeY), end = Offset(leftFootX, feetY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(rightKneeX, kneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(rightKneeX, kneeY), end = Offset(rightFootX, feetY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Hands in prayer position at chest
                    drawCircle(color = AccentCyan, radius = 7.dp.toPx(), center = Offset(shoulderX, shoulderY + 8.dp.toPx()))
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 8. Clamshell Pelvic Stability
                "kegel_clamshell" -> {
                    val headX = w * 0.22f
                    val headY = floorY - (h * 0.12f)
                    val shoulderX = w * 0.36f
                    val shoulderY = floorY - (h * 0.12f)
                    val hipX = w * 0.58f
                    val hipY = floorY - (h * 0.12f)
                    val feetX = w * 0.74f
                    val feetY = floorY - (h * 0.08f)

                    // Upper knee opening like a clamshell
                    val clamOpen = cycleProgress * (h * 0.20f)
                    val lowerKneeX = w * 0.70f
                    val lowerKneeY = floorY - (h * 0.06f)
                    val upperKneeX = w * 0.68f
                    val upperKneeY = (floorY - (h * 0.14f)) - clamOpen

                    // Gluteus medius & pelvic ring glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(SkyBlueLight, Color.Transparent),
                            center = Offset(hipX, hipY),
                            radius = 20.dp.toPx()
                        ),
                        radius = 20.dp.toPx(),
                        center = Offset(hipX, hipY)
                    )

                    // Body
                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)
                    // Lower leg
                    drawLine(color = depthShade, start = Offset(hipX, hipY), end = Offset(lowerKneeX, lowerKneeY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = depthShade, start = Offset(lowerKneeX, lowerKneeY), end = Offset(feetX, feetY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)

                    // Upper opening leg
                    drawLine(color = SkyBlueLight, start = Offset(hipX, hipY), end = Offset(upperKneeX, upperKneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = SkyBlueLight, start = Offset(upperKneeX, upperKneeY), end = Offset(feetX, feetY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 9. Dead Bug Pelvic Core
                "kegel_dead_bug" -> {
                    val drop = cycleProgress * (h * 0.16f)
                    val headX = w * 0.24f
                    val headY = floorY - (h * 0.08f)
                    val shoulderX = w * 0.36f
                    val shoulderY = floorY - (h * 0.08f)
                    val hipX = w * 0.56f
                    val hipY = floorY - (h * 0.08f)

                    // Left Arm up, Right Arm dropping
                    val leftArmY = floorY - (h * 0.32f)
                    val rightArmY = (floorY - (h * 0.32f)) + drop

                    // Left Leg 90 degree, Right Leg extending down
                    val leftKneeX = w * 0.60f
                    val leftKneeY = floorY - (h * 0.32f)
                    val rightKneeX = w * 0.76f + (drop * 0.5f)
                    val rightKneeY = (floorY - (h * 0.30f)) + drop

                    // Pelvic flat core brace glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(SkyBlueLight.copy(alpha = 0.85f), AccentCyan.copy(alpha = 0.4f), Color.Transparent),
                            center = Offset(hipX, hipY),
                            radius = 22.dp.toPx()
                        ),
                        radius = 22.dp.toPx(),
                        center = Offset(hipX, hipY)
                    )

                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)
                    // Arms
                    drawLine(color = AccentCyan, start = Offset(shoulderX, shoulderY), end = Offset(w * 0.34f, leftArmY), strokeWidth = 7.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = depthShade, start = Offset(shoulderX, shoulderY), end = Offset(w * 0.28f, rightArmY), strokeWidth = 7.dp.toPx(), cap = StrokeCap.Round)
                    // Legs
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(leftKneeX, leftKneeY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = depthShade, start = Offset(hipX, hipY), end = Offset(rightKneeX, rightKneeY), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)

                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                // 10. Happy Baby Pelvic Release
                "kegel_happy_baby" -> {
                    val rock = (cycleProgress - 0.5f) * 12.dp.toPx()
                    val headX = w * 0.24f
                    val headY = floorY - (h * 0.08f)
                    val shoulderX = w * 0.36f
                    val shoulderY = floorY - (h * 0.08f)
                    val hipX = w * 0.54f
                    val hipY = floorY - (h * 0.08f)

                    // Knees pulled in wide, feet pointing up
                    val kneeX = w * 0.58f + rock
                    val kneeY = floorY - (h * 0.30f)
                    val footX = w * 0.62f + rock
                    val footY = floorY - (h * 0.42f)

                    // Pelvic floor deep release pulse
                    val releaseR = 26.dp.toPx() + (cycleProgress * 10.dp.toPx())
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(SkyBlueLight.copy(alpha = 0.7f), Color.Transparent),
                            center = Offset(hipX, hipY),
                            radius = releaseR
                        ),
                        radius = releaseR,
                        center = Offset(hipX, hipY)
                    )

                    // Torso
                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)
                    // Legs up
                    drawLine(color = bodyColor, start = Offset(hipX, hipY), end = Offset(kneeX, kneeY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(color = bodyColor, start = Offset(kneeX, kneeY), end = Offset(footX, footY), strokeWidth = 9.dp.toPx(), cap = StrokeCap.Round)

                    // Arms holding feet
                    drawLine(color = AccentCyan, start = Offset(shoulderX, shoulderY), end = Offset(footX, footY), strokeWidth = 6.dp.toPx(), cap = StrokeCap.Round)

                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }

                else -> {
                    // Default fallback
                    val headX = w * 0.28f
                    val headY = floorY - (h * 0.12f)
                    val shoulderX = w * 0.38f
                    val shoulderY = floorY - (h * 0.10f)
                    val hipX = w * 0.58f
                    val hipY = floorY - (h * 0.10f)

                    drawLine(color = skyColor, start = Offset(shoulderX, shoulderY), end = Offset(hipX, hipY), strokeWidth = 11.dp.toPx(), cap = StrokeCap.Round)
                    drawCircle(color = bodyColor, radius = headRadius, center = Offset(headX, headY))
                }
            }
        }

        // 3D Squeeze & Relax indicator badge in Sky Blue
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSqueezing) SkyBlueLight else Color(0x6618233C))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isSqueezing) "সংকোচন (Squeeze)" else "শিথিল (Relax)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSqueezing) Color.Black else SkyBlueLight
            )
        }
    }
}
