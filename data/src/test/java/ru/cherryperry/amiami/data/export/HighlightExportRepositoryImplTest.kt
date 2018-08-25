package ru.cherryperry.amiami.data.export

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.cherryperry.amiami.domain.model.HighlightRule

class HighlightExportRepositoryImplTest {

    @field:Rule
    @JvmField
    var temporaryFolder = TemporaryFolder()

    private val repository = HighlightExportRepositoryImpl()

    @Test
    fun testExport() {
        val file = temporaryFolder.newFile()
        repository.export(listOf(HighlightRule(0, "test", true)), file.outputStream())
            .test()
            .await()
            .assertComplete()
        Assert.assertEquals("""{"highlight":[{"value":"test","regex":true}]}""", file.readText())
    }

    @Test
    fun testImport() {
        val file = temporaryFolder.newFile()
        file.writeText("""{"highlight":[{"value":"test","regex":true}]}""")
        repository.import(file.inputStream())
            .test()
            .await()
            .assertValue(listOf(HighlightRule(0, "test", true)))
    }

    @Test
    fun testImportEmptyFile() {
        val file = temporaryFolder.newFile()
        val inputStream = file.inputStream()
        repository.import(inputStream)
            .test()
            .await()
            .assertError(NullPointerException::class.java)
    }
}
