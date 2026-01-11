package net.hawkins.gamescore.ui.favorites

import net.hawkins.gamescore.data.model.Game
import net.hawkins.gamescore.ui.gameplay.Player
import org.junit.Assert.assertEquals
import org.junit.Test

class GamesTest {

    @Test
    fun buildFavoriteName() {
        val game = Game(name = "Five Crowns")
        val players = listOf(Player("Sheldon"), Player("Leonard"))

        assertEquals("Five Crowns - Sheldon, Leonard", buildFavoriteName(game, players))
    }
}