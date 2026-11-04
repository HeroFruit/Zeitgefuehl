package de.tfc.zeitgefuehl.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.tfc.zeitgefuehl.model.GameStatus

@Composable
fun HoldButton(
    gameStatus: GameStatus,
    onPressDown: () -> Unit,
    onRelease: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val isPressed = gameStatus == GameStatus.RUNNING

    // Button scale down on press
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "ButtonScale"
    )

    // Pulse animation for outer glow while holding
    val infiniteTransition = rememberInfiniteTransition(label = "HoldPulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseAlpha"
    )

    // Animated background colors
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.tertiary
    val activeColor by animateColorAsState(
        targetValue = if (isPressed) secondaryColor else primaryColor,
        animationSpec = tween(300),
        label = "ButtonColor"
    )

    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onSecondaryColor = MaterialTheme.colorScheme.onTertiary
    val activeOnColor by animateColorAsState(
        targetValue = if (isPressed) onSecondaryColor else onPrimaryColor,
        animationSpec = tween(300),
        label = "ButtonOnColor"
    )

    // Keep updated references so pointerInput(Unit) is NEVER restarted during touch
    val currentOnPressDown by rememberUpdatedState(onPressDown)
    val currentOnRelease by rememberUpdatedState(onRelease)

    Box(
        modifier = modifier.size(260.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer pulsing ring when pressed
        if (isPressed) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(activeColor.copy(alpha = pulseAlpha))
            )
        }

        // Main Hold Button
        Box(
            modifier = Modifier
                .size(220.dp)
                .scale(scale)
                .shadow(
                    elevation = if (isPressed) 24.dp else 10.dp,
                    shape = CircleShape,
                    spotColor = activeColor,
                    ambientColor = activeColor
                )
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            activeColor.copy(alpha = if (isPressed) 0.95f else 0.85f),
                            activeColor.copy(alpha = if (isPressed) 0.70f else 0.50f),
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
                .border(
                    width = if (isPressed) 4.dp else 2.dp,
                    brush = Brush.sweepGradient(
                        listOf(
                            activeColor,
                            MaterialTheme.colorScheme.secondary,
                            activeColor
                        )
                    ),
                    shape = CircleShape
                )
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        down.consume()
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        currentOnPressDown()

                        try {
                            waitForUpOrCancellation()
                        } finally {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            currentOnRelease()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.TouchApp,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = activeOnColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isPressed) "FÜHLE DIE ZEIT" else "GEDRÜCKT\nHALTEN",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = if (isPressed) 16.sp else 18.sp,
                        letterSpacing = 1.2.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = activeOnColor
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isPressed) "Bei Ziel loslassen" else "Tippen & Halten",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = activeOnColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}
