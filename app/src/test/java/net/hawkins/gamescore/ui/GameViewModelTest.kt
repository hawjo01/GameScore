package net.hawkins.gamescore.ui

import net.hawkins.gamescore.data.Player
import net.hawkins.gamescore.game.GameType
import org.junit.Assert
import org.junit.Test

class GameViewModelTest {

    @Test
    fun construction() {
        val gameViewModel = GameViewModel()
        Assert.assertEquals(0, gameViewModel.getPlayers().size)
        // 2500 is used the most often so it should be first.
        Assert.assertEquals("2500", gameViewModel.getGameType().getName())
        // Currently only 2 game types are supported
        Assert.assertEquals(2, gameViewModel.gameTypes.size)
    }


    @Test
    fun getGameType() {
        val gameViewModel = GameViewModel()
        Assert.assertEquals("2500", gameViewModel.getGameType().getName())

        val notTwentyFiveHundred =
            gameViewModel.gameTypes.first { it.getName() != "2500" }
        gameViewModel.setGameType(notTwentyFiveHundred)
        Assert.assertEquals(notTwentyFiveHundred, gameViewModel.getGameType())
    }

    @Test
    fun resetGame() {
        val gameViewModel = GameViewModel()
        val expectedGameType = gameViewModel.gameTypes[1]
        gameViewModel.setGameType(expectedGameType)
        Assert.assertEquals(expectedGameType, gameViewModel.getGameType())

        // Should do nothing because no players exist.
        Assert.assertEquals(0, gameViewModel.getPlayers().size)
        gameViewModel.resetGame()
        Assert.assertEquals(0, gameViewModel.getPlayers().size)

        gameViewModel.setPlayers(listOf("Sheldon", "Penny"))
        gameViewModel.getPlayers()[0].addScore(20)
        gameViewModel.getPlayers()[0].addScore(50)

        gameViewModel.getPlayers()[1].addScore(100)
        gameViewModel.getPlayers()[1].addScore(200)

        Assert.assertEquals("Sheldon", gameViewModel.getPlayers()[0].name)
        Assert.assertEquals(listOf(20, 50), gameViewModel.getPlayers()[0].scores)
        Assert.assertEquals(70, gameViewModel.getPlayers()[0].totalScore())

        Assert.assertEquals("Penny", gameViewModel.getPlayers()[1].name)
        Assert.assertEquals(listOf(100, 200), gameViewModel.getPlayers()[1].scores)
        Assert.assertEquals(300, gameViewModel.getPlayers()[1].totalScore())

        gameViewModel.resetGame()
        Assert.assertEquals(2, gameViewModel.getPlayers().size)
        Assert.assertEquals("Sheldon", gameViewModel.getPlayers()[0].name)
        Assert.assertEquals(listOf<Int>(), gameViewModel.getPlayers()[0].scores)
        Assert.assertEquals(0, gameViewModel.getPlayers()[0].totalScore())

        Assert.assertEquals("Penny", gameViewModel.getPlayers()[1].name)
        Assert.assertEquals(listOf<Int>(), gameViewModel.getPlayers()[1].scores)
        Assert.assertEquals(0, gameViewModel.getPlayers()[1].totalScore())
        Assert.assertEquals(expectedGameType, gameViewModel.getGameType())
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
        Assert.assertEquals("7", gameViewModel.getGameType().getName())
        Assert.assertFalse(gameViewModel.highlightNegativeScore())
        Assert.assertTrue(gameViewModel.hasWinningThreshold())

        Assert.assertTrue(gameViewModel.isValidScore("7"))
        Assert.assertFalse(gameViewModel.isValidScore("1"))
        gameViewModel.setPlayers(listOf("Penny", "Bernadette"))
        gameViewModel.getPlayers()[0].addScore(7)

        gameViewModel.getPlayers()[1].addScore(14)

        Assert.assertNull(gameViewModel.determineWinner())

        gameViewModel.getPlayers()[0].addScore(70)
        gameViewModel.getPlayers()[1].addScore(21)

        val winner = gameViewModel.determineWinner()
        Assert.assertNotNull(winner)
        Assert.assertEquals("Penny", winner?.name)
        Assert.assertNotNull(gameViewModel.getWinner())
        Assert.assertEquals("Penny", gameViewModel.getWinner()?.name)

        gameViewModel.resetGame()
        Assert.assertNull(gameViewModel.getWinner())
    }
}
