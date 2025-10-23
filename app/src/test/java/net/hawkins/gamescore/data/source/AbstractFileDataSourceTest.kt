package net.hawkins.gamescore.data.source

import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.UUID

abstract class AbstractFileDataSourceTest {
    @Rule
    @JvmField
    val tempDir: TemporaryFolder = TemporaryFolder()

    fun randomTempFile(): File {
        val tempFile = File(tempDir.root, "testfile_" + UUID.randomUUID().toString() + ".json")
        assertFalse(tempFile.exists())
        return tempFile
    }
}