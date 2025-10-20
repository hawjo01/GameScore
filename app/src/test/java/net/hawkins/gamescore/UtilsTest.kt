package net.hawkins.gamescore

import org.junit.Assert.*
import org.junit.Test

class UtilsTest {

    @Test
    fun isNegativeNumber_String() {
        assertTrue(Utils.isNegativeInt("-5"))
        assertFalse(Utils.isNegativeInt(""))
        assertFalse(Utils.isNegativeInt("0"))
        assertFalse(Utils.isNegativeInt("5"))
        assertFalse(Utils.isNegativeInt(".5"))
        assertFalse(Utils.isNegativeInt("-.5"))
        assertFalse(Utils.isNegativeInt("-0"))
        assertFalse(Utils.isNegativeInt("a"))
    }

    @Test
    fun isNegativeNumber_Int() {
        assertTrue(Utils.isNegativeInt(-1))
        assertFalse(Utils.isNegativeInt(0))
        assertFalse(Utils.isNegativeInt(1))
    }
}