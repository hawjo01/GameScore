package net.hawkins.gamescore.service.gameplay

import net.hawkins.gamescore.ui.gameplay.Player
import net.hawkins.gamescore.ui.gameplay.Score

abstract class AbstractGamePlayTest {

    fun createPlayer(name: String, values: List<Int> = emptyList()): Player {
        val scores = values.map { value -> Score(value = value)}
        return Player(name, scores)
    }
}