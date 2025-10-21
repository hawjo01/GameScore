package net.hawkins.gamescore.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import net.hawkins.gamescore.game.Player

class GameUiState {
    val players = mutableStateListOf<Player>()
    var winner = mutableStateOf<Player?>(null)
}