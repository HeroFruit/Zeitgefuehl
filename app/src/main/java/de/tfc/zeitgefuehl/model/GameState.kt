package de.tfc.zeitgefuehl.model

enum class GameStatus {
    IDLE,
    RUNNING,
    FINISHED
}

data class GameResult(
    val targetTimeMillis: Long,
    val elapsedTimeMillis: Long
) {
    val diffMillis: Long = elapsedTimeMillis - targetTimeMillis
    val absDiffMillis: Long = kotlin.math.abs(diffMillis)

    val accuracyPercentage: Double = run {
        if (targetTimeMillis <= 0) 0.0
        else {
            val score = 100.0 - (absDiffMillis.toDouble() / targetTimeMillis.toDouble()) * 100.0
            score.coerceIn(0.0, 100.0)
        }
    }

    val formattedTarget: String = String.format(java.util.Locale.GERMANY, "%.3f s", targetTimeMillis / 1000f)
    val formattedElapsed: String = String.format(java.util.Locale.GERMANY, "%.3f s", elapsedTimeMillis / 1000f)
    val formattedDiff: String = run {
        val sign = if (diffMillis > 0) "+" else if (diffMillis < 0) "-" else "±"
        "$sign$absDiffMillis ms"
    }

    val feedbackMessage: String = when {
        absDiffMillis <= 50 -> "Unmenschliche Präzision! ⚡"
        absDiffMillis <= 150 -> "Perfektes Zeitgefühl! 🎯"
        absDiffMillis <= 350 -> "Hervorragend! ⏱️"
        absDiffMillis <= 750 -> "Gutes Gespür! 👍"
        absDiffMillis <= 1500 -> "Guter Versuch! ⏳"
        else -> "Etwas daneben – Noch ein Versuch? 🔄"
    }

    val ratingStars: Int = when {
        absDiffMillis <= 80 -> 5
        absDiffMillis <= 250 -> 4
        absDiffMillis <= 500 -> 3
        absDiffMillis <= 1000 -> 2
        else -> 1
    }
}

data class GameUiState(
    val targetSeconds: Float = 5.0f,
    val gameStatus: GameStatus = GameStatus.IDLE,
    val elapsedMillis: Long = 0L,
    val isTimerVisible: Boolean = true,
    val isTargetDialogOpen: Boolean = false,
    val lastResult: GameResult? = null,
    val isDarkTheme: Boolean? = null
) {
    val targetMillis: Long get() = (targetSeconds * 1000).toLong()
}
