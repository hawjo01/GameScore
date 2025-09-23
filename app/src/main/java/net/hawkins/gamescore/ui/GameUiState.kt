package net.hawkins.gamescore.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import net.hawkins.gamescore.data.Player

enum class GameState {
    SETUP,
    PLAY
}

class GameUiState {
    val players = mutableStateListOf<Player>()
    var gameState = mutableStateOf(GameState.SETUP)
}