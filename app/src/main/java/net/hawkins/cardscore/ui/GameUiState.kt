package net.hawkins.cardscore.ui

import androidx.compose.runtime.mutableStateListOf
import net.hawkins.cardscore.data.Player

class GameUiState {
    val players = mutableStateListOf<Player>()
    val playerNames = mutableStateListOf<String>()
}