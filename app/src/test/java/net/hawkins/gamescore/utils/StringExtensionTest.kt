package net.hawkins.gamescore.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionTest {

    @Test
    fun isNegativeInt() {
        assertTrue("-5".isNegativeInt())
        assertFalse("".isNegativeInt())
        assertFalse("0".isNegativeInt())
        assertFalse("5".isNegativeInt())
        assertFalse(".5".isNegativeInt())
        assertFalse("-.5".isNegativeInt())
        assertFalse("-0".isNegativeInt())
        assertFalse("a".isNegativeInt())
    }
}