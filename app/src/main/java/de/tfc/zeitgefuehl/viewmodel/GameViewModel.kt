package de.tfc.zeitgefuehl.viewmodel

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.tfc.zeitgefuehl.model.GameResult
import de.tfc.zeitgefuehl.model.GameStatus
import de.tfc.zeitgefuehl.model.GameUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var startTimestamp: Long = 0L

    fun onButtonPressDown() {
        if (_uiState.value.gameStatus == GameStatus.RUNNING) return

        timerJob?.cancel()
        startTimestamp = SystemClock.elapsedRealtime()

        _uiState.update {
            it.copy(
                gameStatus = GameStatus.RUNNING,
                elapsedMillis = 0L,
                isTimerVisible = true,
                lastResult = null
            )
        }

        timerJob = viewModelScope.launch {
            while (true) {
                val now = SystemClock.elapsedRealtime()
                val elapsed = now - startTimestamp

                val shouldTimerBeVisible = elapsed < 1000L

                _uiState.update { current ->
                    if (current.gameStatus != GameStatus.RUNNING) current
                    else current.copy(
                        elapsedMillis = elapsed,
                        isTimerVisible = shouldTimerBeVisible
                    )
                }

                delay(16.milliseconds) // ~60fps smooth ticker for the first second
            }
        }
    }

    fun onButtonRelease() {
        if (_uiState.value.gameStatus != GameStatus.RUNNING) return

        timerJob?.cancel()
        timerJob = null

        val endTimestamp = SystemClock.elapsedRealtime()
        val totalElapsed = endTimestamp - startTimestamp
        val targetMillis = _uiState.value.targetMillis
        val result = GameResult(
            targetTimeMillis = targetMillis,
            elapsedTimeMillis = totalElapsed
        )

        _uiState.update {
            it.copy(
                gameStatus = GameStatus.FINISHED,
                elapsedMillis = totalElapsed,
                isTimerVisible = true,
                lastResult = result
            )
        }
    }

    fun openTargetDialog() {
        if (_uiState.value.gameStatus == GameStatus.RUNNING) return
        _uiState.update { it.copy(isTargetDialogOpen = true) }
    }

    fun closeTargetDialog() {
        _uiState.update { it.copy(isTargetDialogOpen = false) }
    }

    fun setTargetSeconds(seconds: Float) {
        val clamped = seconds.coerceIn(1.0f, 60.0f)
        _uiState.update {
            it.copy(
                targetSeconds = clamped,
                isTargetDialogOpen = false,
                gameStatus = GameStatus.IDLE,
                elapsedMillis = 0L,
                isTimerVisible = true,
                lastResult = null
            )
        }
    }

    fun dismissResult() {
        _uiState.update {
            it.copy(
                gameStatus = GameStatus.IDLE,
                elapsedMillis = 0L,
                isTimerVisible = true,
                lastResult = null
            )
        }
    }

    fun toggleTheme(currentIsDark: Boolean) {
        _uiState.update { it.copy(isDarkTheme = !currentIsDark) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
