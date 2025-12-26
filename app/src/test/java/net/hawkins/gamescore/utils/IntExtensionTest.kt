package net.hawkins.gamescore.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IntExtensionTest {

    @Test
    fun isNegativeInt() {
        val negative1 = -1
        assertTrue(negative1.isNegative())

        assertFalse(0.isNegative())
        assertFalse(1.isNegative())
    }
}