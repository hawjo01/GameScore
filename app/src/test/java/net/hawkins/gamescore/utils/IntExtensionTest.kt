package net.hawkins.gamescore.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IntExtensionTest {

    @Test
    fun isNegativeInt() {
        assertTrue((-1).isNegative())

        assertFalse(0.isNegative())
        assertFalse(1.isNegative())
    }

    @Test
    fun isEven() {
        assertTrue((-2).isEven())
        assertTrue(0.isEven())
        assertTrue(2.isEven())

        assertFalse(1.isEven())
        assertFalse((-1).isEven())
    }
}