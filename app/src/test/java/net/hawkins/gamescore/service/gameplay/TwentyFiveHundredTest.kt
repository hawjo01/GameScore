package net.hawkins.gamescore.service.gameplay

import net.hawkins.gamescore.TestData
import net.hawkins.gamescore.service.GamePlayService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TwentyFiveHundredTest {

    @Test
    fun isValidScore_true() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        assertTrue(gamePlayService.isValidScore("0"))
        assertTrue(gamePlayService.isValidScore("5"))
        assertTrue(gamePlayService.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        assertFalse(gamePlayService.isValidScore(""))
        assertFalse(gamePlayService.isValidScore("1"))
        assertFalse(gamePlayService.isValidScore("-1"))
        assertFalse(gamePlayService.isValidScore(".5"))
        assertFalse(gamePlayService.isValidScore("a"))
    }

    @Test
    fun isManualWinner() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        assertFalse(gamePlayService.isManualWinner())
    }

    @Test
    fun findWinner_zeroPlayers() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        assertNull(gamePlayService.determineWinner(emptyList()))
    }

    @Test
    fun findWinner_differentScores() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        val player1 = TestData.createPlayer("Sheldon", listOf(20))
        val player2 = TestData.createPlayer("Penny")
        val players = listOf(player1, player2)

        assertNull(gamePlayService.determineWinner(players))
    }

    @Test
    fun findWinner_ThresholdNotMet() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        val player1 = TestData.createPlayer("Sheldon", listOf(20))
        val player2 = TestData.createPlayer("Penny", listOf(50))
        val players = listOf(player1, player2)

        assertNull(gamePlayService.determineWinner(players))
    }

    @Test
    fun findWinner_ThresholdMet() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        val player1 = TestData.createPlayer("Sheldon", listOf(20))
        val player2 = TestData.createPlayer("Penny", listOf(2500))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertEquals(player2.name, winner)
    }

    @Test
    fun findWinner_ThresholdMet_UnequalHands() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        val player1 = TestData.createPlayer("Sheldon", listOf(20))
        val player2 = TestData.createPlayer("Penny", listOf(1000, 1500))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertNull(winner)
    }

    @Test
    fun findWinner_ThresholdExceeded() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        val player1 = TestData.createPlayer("Sheldon", listOf(2600))
        val player2 = TestData.createPlayer("Penny", listOf(2505))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertEquals(player1.name, winner)
    }

    @Test
    fun findWinner_ThresholdMet_EqualsScores() {
        val twentyFiveHundred = TestData.getTwentyFiveHundred()
        val gamePlayService = GamePlayService(twentyFiveHundred)
        val player1 = TestData.createPlayer("Sheldon", listOf(2600))
        val player2 = TestData.createPlayer("Penny", listOf(2600))
        val players = listOf(player1, player2)

        val winner = gamePlayService.determineWinner(players)
        assertNull(winner)
    }
}