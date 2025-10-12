package net.hawkins.gamescore.ui

import androidx.compose.runtime.mutableStateListOf
import net.hawkins.gamescore.data.Player

class GameUiState {
    val players = mutableStateListOf<Player>()
}