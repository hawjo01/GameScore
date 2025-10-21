package net.hawkins.gamescore.ui

import net.hawkins.gamescore.game.Player
import net.hawkins.gamescore.game.GameType
import net.hawkins.gamescore.game.Games
import org.junit.Assert.*
import org.junit.Test

class GameViewModelTest {

    @Test
    fun construction() {
        val gameViewModel = GameViewModel()
        assertEquals(0, gameViewModel.getPlayers().size)
        // 2500 is used the most often so it should be first.
        assertEquals("2500", gameViewModel.getGameType().getName())
    }


    @Test
    fun getGameType() {
        val gameViewModel = GameViewModel()
        assertEquals("2500", gameViewModel.getGameType().getName())

        val notTwentyFiveHundred =
            Games.TYPES.first { it.getName() != "2500" }
        gameViewModel.setGameType(notTwentyFiveHundred)
        assertEquals(notTwentyFiveHundred, gameViewModel.getGameType())
    }

    @Test
    fun resetGame() {
        val gameViewModel = GameViewModel()
        val expectedGameType = Games.TYPES[1]
        gameViewModel.setGameType(expectedGameType)
        assertEquals(expectedGameType, gameViewModel.getGameType())

        // Should do nothing because no players exist.
        assertEquals(0, gameViewModel.getPlayers().size)
        gameViewModel.resetGame()
        assertEquals(0, gameViewModel.getPlayers().size)

        gameViewModel.setPlayers(listOf("Sheldon", "Penny"))
        gameViewModel.getPlayers()[0].addScore(20)
        gameViewModel.getPlayers()[0].addScore(50)

        gameViewModel.getPlayers()[1].addScore(100)
        gameViewModel.getPlayers()[1].addScore(200)

        assertEquals("Sheldon", gameViewModel.getPlayers()[0].name)
        assertEquals(listOf(20, 50), gameViewModel.getPlayers()[0].scores)
        assertEquals(70, gameViewModel.getPlayers()[0].totalScore())

        assertEquals("Penny", gameViewModel.getPlayers()[1].name)
        assertEquals(listOf(100, 200), gameViewModel.getPlayers()[1].scores)
        assertEquals(300, gameViewModel.getPlayers()[1].totalScore())

        gameViewModel.resetGame()
        assertEquals(2, gameViewModel.getPlayers().size)
        assertEquals("Sheldon", gameViewModel.getPlayers()[0].name)
        assertEquals(listOf<Int>(), gameViewModel.getPlayers()[0].scores)
        assertEquals(0, gameViewModel.getPlayers()[0].totalScore())

        assertEquals("Penny", gameViewModel.getPlayers()[1].name)
        assertEquals(listOf<Int>(), gameViewModel.getPlayers()[1].scores)
        assertEquals(0, gameViewModel.getPlayers()[1].totalScore())
        assertEquals(expectedGameType, gameViewModel.getGameType())
    }

    @Test
    fun gameType() {
        class Seven : GameType {

            override fun getName(): String {
                return "7"
            }

            override fun isValidScore(score: String): Boolean {
                return (score.toIntOrNull() != null && score.toInt() % 7 == 0)
            }

            override fun findWinner(players: List<Player>): Player? {
                return players.firstOrNull { player -> player.totalScore() >= 77 }
            }

            override fun highlightNegativeScore(): Boolean {
                return false
            }

            override fun hasWinningThreshold(): Boolean {
                return true
            }
        }

        val gameViewModel = GameViewModel()
        gameViewModel.setGameType(Seven())
        assertEquals("7", gameViewModel.getGameType().getName())
        assertFalse(gameViewModel.highlightNegativeScore())
        assertTrue(gameViewModel.hasWinningThreshold())

        assertTrue(gameViewModel.isValidScore("7"))
        assertFalse(gameViewModel.isValidScore("1"))
        gameViewModel.setPlayers(listOf("Penny", "Bernadette"))
        gameViewModel.getPlayers()[0].addScore(7)

        gameViewModel.getPlayers()[1].addScore(14)

        assertNull(gameViewModel.determineWinner())

        gameViewModel.getPlayers()[0].addScore(70)
        gameViewModel.getPlayers()[1].addScore(21)

        val winner = gameViewModel.determineWinner()
        assertNotNull(winner)
        assertEquals("Penny", winner?.name)
        assertNotNull(gameViewModel.getWinner())
        assertEquals("Penny", gameViewModel.getWinner()?.name)

        gameViewModel.resetGame()
        assertNull(gameViewModel.getWinner())
    }
}
