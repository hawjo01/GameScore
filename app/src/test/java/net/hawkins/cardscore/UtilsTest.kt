package net.hawkins.cardscore

import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun isNegativeNumber() {
        Assert.assertTrue(Utils.isNegativeInt("-5"))
        Assert.assertFalse(Utils.isNegativeInt(""))
        Assert.assertFalse(Utils.isNegativeInt("0"))
        Assert.assertFalse(Utils.isNegativeInt("5"))
        Assert.assertFalse(Utils.isNegativeInt(".5"))
        Assert.assertFalse(Utils.isNegativeInt("-.5"))
        Assert.assertFalse(Utils.isNegativeInt("-0"))
    }
}