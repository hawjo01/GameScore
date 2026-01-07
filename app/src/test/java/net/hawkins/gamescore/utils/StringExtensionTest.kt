package net.hawkins.gamescore.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
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

    @Test
    fun trimToNull() {
        assertNull("".trimToNull())
        assertNull(" ".trimToNull())

        assertEquals("a", "a".trimToNull())
        assertEquals("a", " a ".trimToNull())
        assertEquals("a b", " a b ".trimToNull())
    }
}