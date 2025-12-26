package net.hawkins.gamescore.ui.gamesetup

import net.hawkins.gamescore.data.model.Game

data class GameSetupUiState(
    val game: Game = Game(),
    val mode: Mode = Mode.NEW
) {
    enum class Mode {
        NEW,
        VIEW,
        EDIT
    }
}
