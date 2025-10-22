package net.hawkins.gamescore.ui

data class GameSetupUiState(
    val gameName: String = "",
    val playerNames: List<String> = emptyList()
)