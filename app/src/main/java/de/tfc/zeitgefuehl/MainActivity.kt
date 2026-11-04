package de.tfc.zeitgefuehl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.tfc.zeitgefuehl.ui.screens.GameScreen
import de.tfc.zeitgefuehl.ui.theme.ZeitgefuehlTheme
import de.tfc.zeitgefuehl.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: GameViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val systemDark = isSystemInDarkTheme()
            val isDarkTheme = uiState.isDarkTheme ?: systemDark

            ZeitgefuehlTheme(
                darkTheme = isDarkTheme,
                dynamicColor = true
            ) {
                GameScreen(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { viewModel.toggleTheme(isDarkTheme) }
                )
            }
        }
    }
}
