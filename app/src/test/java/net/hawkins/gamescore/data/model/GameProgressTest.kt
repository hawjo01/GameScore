package net.hawkins.gamescore.data.model

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameProgressTest {

    @Test
    fun isComplete_True() {
        val gameProgress = GameProgress(
            game = Game(name = "Test Game"),
            players = emptyList(),
            winner = "Winner"
        )
        assertTrue(gameProgress.isComplete())
    }

    @Test
    fun isComplete_False() {
        val gameProgress = GameProgress(
            game = Game(name = "Test Game"),
            players = emptyList(),
            winner = null
        )
        assertFalse(gameProgress.isComplete())
    }
}
