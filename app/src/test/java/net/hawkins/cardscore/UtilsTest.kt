package net.hawkins.cardscore

import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun isNegativeNumber_String() {
        Assert.assertTrue(Utils.isNegativeInt("-5"))
        Assert.assertFalse(Utils.isNegativeInt(""))
        Assert.assertFalse(Utils.isNegativeInt("0"))
        Assert.assertFalse(Utils.isNegativeInt("5"))
        Assert.assertFalse(Utils.isNegativeInt(".5"))
        Assert.assertFalse(Utils.isNegativeInt("-.5"))
        Assert.assertFalse(Utils.isNegativeInt("-0"))
    }

    @Test
    fun isNegativeNumber_Int() {
        Assert.assertTrue(Utils.isNegativeInt(-1))
        Assert.assertFalse(Utils.isNegativeInt(0))
        Assert.assertFalse(Utils.isNegativeInt(1))
    }
}