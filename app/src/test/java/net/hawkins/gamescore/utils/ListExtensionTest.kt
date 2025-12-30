package net.hawkins.gamescore.utils

import org.junit.Test
import kotlin.test.assertEquals

class ListExtensionTest {

    @Test
    fun replaceElementAtIndex_ReplaceFirstElement() {
        val list1 = listOf(1, 2)
        val updatedList = list1.replaceElementAtIndex(0, -1)
        assertEquals( listOf(-1, 2), updatedList)
    }

    @Test
    fun replaceElementAtIndex_ReplaceLastElement() {
        val list1 = listOf(1, 2)
        val updatedList = list1.replaceElementAtIndex(1, -1)
        assertEquals( listOf(1, -1), updatedList)
    }

    @Test
    fun replaceElementAtIndex_IndexOutOfRange_ListNotChanged() {
        val list1 = listOf(1, 2)
        val updatedList = list1.replaceElementAtIndex(2, -1)
        assertEquals( list1, updatedList)
    }

    @Test
    fun removeElementAtIndex_RemoveFirstElement() {
        val list1 = listOf(1, 2)
        val updatedList = list1.removeElementAtIndex(0)
        assertEquals(listOf(2), updatedList)
    }

    @Test
    fun removeElementAtIndex_RemoveLastElement() {
        val list1 = listOf(1, 2)
        val updatedList = list1.removeElementAtIndex(1)
        assertEquals(listOf(1), updatedList)
    }

    @Test
    fun removeElementAtIndex_IndexOutOfRange_ListNotChanged() {
        val list1 = listOf(1, 2)
        val updatedList = list1.removeElementAtIndex(2)
        assertEquals(list1, updatedList)
    }
}