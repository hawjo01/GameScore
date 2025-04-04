package net.hawkins.cardscore

import org.junit.Assert.*
import org.junit.Test

class RummyTest {

    @Test
    fun isValidScore_true() {
        assertTrue(Rummy.isValidScore("0"))
        assertTrue(Rummy.isValidScore("5"))
        assertTrue(Rummy.isValidScore("-5"))
    }

    @Test
    fun isValidScore_false() {
        assertFalse(Rummy.isValidScore(""))
        assertFalse(Rummy.isValidScore("1"))
        assertFalse(Rummy.isValidScore("-1"))
        assertFalse(Rummy.isValidScore(".5"))
    }
}