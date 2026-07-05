package net.hawkins.gamescore.data.model

import org.junit.Test
import kotlin.test.assertEquals

class FavoriteGameTest {

    @Test
    fun constructor_PropertiesAreSet() {
        val game = Game(name = "Catan")
        val players = listOf("Alice", "Bob")
        val favoriteGame = FavoriteGame(
            name = "Friday Night Catan",
            players = players,
            game = game,
            id = 123L
        )

        assertEquals("Friday Night Catan", favoriteGame.name)
        assertEquals(players, favoriteGame.players)
        assertEquals(game, favoriteGame.game)
        assertEquals(123L, favoriteGame.id)
    }
}
