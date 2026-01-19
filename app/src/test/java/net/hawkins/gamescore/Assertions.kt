package net.hawkins.gamescore

import net.hawkins.gamescore.ui.gameplay.Player
import net.hawkins.gamescore.ui.gameplay.Score
import org.junit.Assert.assertEquals

class Assertions {
    companion object {
        fun assertEquals(expected: Player, actual: Player) {
            assertEquals(expected.name, actual.name)
            assertEquals(expected.scores, actual.scores)
        }

        fun assertEquals(expected: List<Score>, actual: List<Score>) {
            assertEquals(expected.size, actual.size)
            expected.forEachIndexed { index, expectedScore ->
                assertEquals(expectedScore, actual[index])
            }
        }

        fun assertEquals(expected: Score, actual: Score) {
            assertEquals(expected.value, actual.value)
            assertEquals(expected.color, actual.color)
            assertEquals(expected.displayValue, actual.displayValue)
        }
    }
}