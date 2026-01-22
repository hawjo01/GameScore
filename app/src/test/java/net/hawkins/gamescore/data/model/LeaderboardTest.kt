package net.hawkins.gamescore.data.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LeaderboardTest {

    @Test
    fun toDataList_Empty() {
        val leaderboard = Leaderboard()
        val dataList = leaderboard.toDataList()
        assertTrue(dataList.isEmpty())
    }

    @Test
    fun toDataList() {
        val leaderboard = Leaderboard(
            gameName = "Scrabble",
            rankings = listOf(
                Leaderboard.Ranking(1, 15, listOf("Penny")),
                Leaderboard.Ranking(2, 10, listOf("Amy"))
            )
        )
        val dataList = leaderboard.toDataList()
        assertEquals(2, dataList.size)

        val row1 = dataList[0]
        assertEquals("1", row1[0])
        assertEquals("Penny", row1[1])
        assertEquals("15", row1[2])

        val row2 = dataList[1]
        assertEquals("2", row2[0])
        assertEquals("Amy", row2[1])
        assertEquals("10", row2[2])
    }

    @Test
    fun toDataList_TieForFirst() {
        val leaderboard = Leaderboard(
            gameName = "Scrabble",
            rankings = listOf(
                Leaderboard.Ranking(1, 15, listOf("Penny", "Amy")),
            )
        )
        val dataList = leaderboard.toDataList()
        assertEquals(1, dataList.size)

        val row1 = dataList[0]
        assertEquals("1", row1[0])
        assertEquals("Penny, Amy", row1[1])
        assertEquals("15", row1[2])
    }
}