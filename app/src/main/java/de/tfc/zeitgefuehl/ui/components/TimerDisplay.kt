package de.tfc.zeitgefuehl.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.tfc.zeitgefuehl.model.GameStatus
import java.util.Locale

@Composable
fun TimerDisplay(
    elapsedMillis: Long,
    targetSeconds: Float,
    isTimerVisible: Boolean,
    gameStatus: GameStatus,
    modifier: Modifier = Modifier
) {
    // Smooth alpha animation for timer fade-out after 1 second
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isTimerVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "TimerAlphaAnimation"
    )

    // Formatted time string
    val displaySeconds = when (gameStatus) {
        GameStatus.IDLE -> 0.0f
        GameStatus.RUNNING, GameStatus.FINISHED -> elapsedMillis / 1000f
    }
    val timeFormatted = String.format(Locale.GERMANY, "%04.2f", displaySeconds)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status Badge / Hint
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(
                    when (gameStatus) {
                        GameStatus.RUNNING -> if (isTimerVisible) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                        GameStatus.FINISHED -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                        GameStatus.IDLE -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    }
                )
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (gameStatus == GameStatus.RUNNING && !isTimerVisible) {
                    Icon(
                        imageVector = Icons.Rounded.VisibilityOff,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Text(
                    text = when (gameStatus) {
                        GameStatus.IDLE -> "Ziel: ${String.format(Locale.GERMANY, "%.1f", targetSeconds)}s • Blendet nach 1s aus"
                        GameStatus.RUNNING -> if (isTimerVisible) "Timer läuft sichtbar..." else "Unsichtbar – Jetzt nach Gefühl!"
                        GameStatus.FINISHED -> "Messung beendet"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Timer Digits with animated alpha (keeps bounds so layout doesn't jump)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            // When hidden during running, show a subtle stealth indicator placeholder
            if (gameStatus == GameStatus.RUNNING && animatedAlpha < 0.95f) {
                Text(
                    text = "• • •",
                    style = MaterialTheme.typography.displayMedium.copy(
                        letterSpacing = 8.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = (1f - animatedAlpha) * 0.35f)
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier.graphicsLayer {
                    alpha = animatedAlpha
                    scaleX = 0.92f + (0.08f * animatedAlpha)
                    scaleY = 0.92f + (0.08f * animatedAlpha)
                },
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 68.sp
                    ),
                    color = if (gameStatus == GameStatus.RUNNING) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = " s",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        fontSize = 24.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}
