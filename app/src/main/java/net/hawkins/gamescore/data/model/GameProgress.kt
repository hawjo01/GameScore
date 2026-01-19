package net.hawkins.gamescore.data.model

import net.hawkins.gamescore.ui.gameplay.Player

data class GameProgress(
    override var id: Int = 0,
    val game: Game,
    val players: List<Player>,
    val winner: String?
) : Idable {

    fun isComplete(): Boolean {
        return winner != null
    }
}