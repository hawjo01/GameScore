package net.hawkins.gamescore

import org.junit.Assert
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.UUID

abstract class AbstractBaseTest {
    @Rule
    @JvmField
    val tempDir: TemporaryFolder = TemporaryFolder()

    fun randomTempFile(extension: String = ".json", create: Boolean = false): File {
        val tempFile = File(tempDir.root, "testfile_" + UUID.randomUUID().toString() + extension)
        Assert.assertFalse(tempFile.exists())
        if (create) {
            Assert.assertTrue(tempFile.createNewFile())
        }
        return tempFile
    }

    fun randomTempDir(create: Boolean = false): File {
        val tempDir = File(tempDir.root, "testfile_" + UUID.randomUUID().toString())
        Assert.assertFalse(tempDir.exists())
        if (create) {
            Assert.assertTrue(tempDir.mkdir())
        }
        return tempDir
    }
}