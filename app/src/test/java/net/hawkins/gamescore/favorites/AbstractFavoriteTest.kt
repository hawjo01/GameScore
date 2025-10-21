package net.hawkins.gamescore.favorites

import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.UUID

abstract class AbstractFavoriteTest {
    @Rule
    @JvmField
    val tempDir: TemporaryFolder = TemporaryFolder()

    fun randomTempFile(): File {
        val tempFile = File(tempDir.root, "testfile_" + UUID.randomUUID().toString() + ".json")
        assertFalse(tempFile.exists())
        return tempFile
    }
}