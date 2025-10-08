package net.hawkins.gamescore.ui

import net.hawkins.gamescore.R
import net.hawkins.gamescore.data.Player
import net.hawkins.gamescore.game.GameType
import org.junit.Assert
import org.junit.Test

class GameViewModelTest {

    @Test
    fun construction() {
        val gameViewModel = GameViewModel()
        Assert.assertTrue(gameViewModel.isGameSetup())
        Assert.assertFalse(gameViewModel.isGamePlay())
        Assert.assertEquals(0, gameViewModel.getPlayers().size)
        // 2500 is used the most often so it should be first.
        Assert.assertEquals(R.string.twenty_five_hundred, gameViewModel.getGameType().getNameId())
        // Currently only 2 game types are supported
        Assert.assertEquals(2, gameViewModel.getGameTypes().size)
    }


    @Test
    fun getGameType() {
        val gameViewModel = GameViewModel()
        Assert.assertEquals(R.string.twenty_five_hundred, gameViewModel.getGameType().getNameId())

        val notTwentyFiveHundred =
            gameViewModel.getGameTypes().first { it.getNameId() != R.string.twenty_five_hundred }
        gameViewModel.setGameType(notTwentyFiveHundred)
        Assert.assertEquals(notTwentyFiveHundred, gameViewModel.getGameType())
    }

    @Test
    fun startGame() {
        val gameViewModel = GameViewModel()
        Assert.assertTrue(gameViewModel.isGameSetup())
        Assert.assertFalse(gameViewModel.isGamePlay())

        gameViewModel.startGame()
        Assert.assertFalse(gameViewModel.isGameSetup())
        Assert.assertTrue(gameViewModel.isGamePlay())
    }

    @Test
    fun players() {
        val gameViewModel = GameViewModel()
        Assert.assertEquals(0, gameViewModel.getPlayers().size)

        gameViewModel.addPlayer("Howard")
        Assert.assertEquals(1, gameViewModel.getPlayers().size)
        Assert.assertEquals("Howard", gameViewModel.getPlayers()[0].name)

        gameViewModel.addPlayer("Raj")
        Assert.assertEquals(2, gameViewModel.getPlayers().size)
        Assert.assertEquals("Howard", gameViewModel.getPlayers()[0].name)
        Assert.assertEquals("Raj", gameViewModel.getPlayers()[1].name)

        // Duplicate player names are allowed
        gameViewModel.addPlayer("Howard")
        Assert.assertEquals(3, gameViewModel.getPlayers().size)
        Assert.assertEquals("Howard", gameViewModel.getPlayers()[0].name)
        Assert.assertEquals("Raj", gameViewModel.getPlayers()[1].name)
        Assert.assertEquals("Howard", gameViewModel.getPlayers()[2].name)

        // Remove the 1st Howard,
        gameViewModel.removePlayer(0)
        Assert.assertEquals(2, gameViewModel.getPlayers().size)
        Assert.assertEquals("Raj", gameViewModel.getPlayers()[0].name)
        Assert.assertEquals("Howard", gameViewModel.getPlayers()[1].name)
    }

    @Test
    fun resetGame() {
        val gameViewModel = GameViewModel()
        val expectedGameType = gameViewModel.getGameTypes()[1]
        gameViewModel.setGameType(expectedGameType)
        Assert.assertEquals(expectedGameType, gameViewModel.getGameType())

        // Should do nothing because no players exist.
        Assert.assertEquals(0, gameViewModel.getPlayers().size)
        gameViewModel.resetGame()
        Assert.assertEquals(0, gameViewModel.getPlayers().size)

        gameViewModel.addPlayer("Sheldon")
        gameViewModel.getPlayers()[0].addScore(20)
        gameViewModel.getPlayers()[0].addScore(50)

        gameViewModel.addPlayer("Penny")
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
            override fun getNameId(): Int {
                return 7
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
        Assert.assertEquals(7, gameViewModel.getGameType().getNameId())
        Assert.assertFalse(gameViewModel.highlightNegativeScore())
        Assert.assertTrue(gameViewModel.hasWinningThreshold())

        Assert.assertTrue(gameViewModel.isValidScore("7"))
        Assert.assertFalse(gameViewModel.isValidScore("1"))
        gameViewModel.addPlayer("Penny")
        gameViewModel.getPlayers()[0].addScore(7)

        gameViewModel.addPlayer("Bernadette")
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
