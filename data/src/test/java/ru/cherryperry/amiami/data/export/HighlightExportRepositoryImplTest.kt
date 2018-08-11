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
        repository.export(listOf(HighlightRule("test")), file.outputStream())
            .test()
            .awaitTerminalEvent()
            .assertNoErrors()
            .assertCompleted()
        Assert.assertEquals("{\"highlight\":[\"test\"]}", file.readText())
    }

    @Test
    fun testImport() {
        val file = temporaryFolder.newFile()
        file.writeText("{\"highlight\":[\"test\"]}")
        repository.import(file.inputStream())
            .test()
            .awaitTerminalEvent()
            .assertNoErrors()
            .assertValue(listOf(HighlightRule("test")))
    }

    @Test
    fun testImportEmptyFile() {
        val file = temporaryFolder.newFile()
        val inputStream = file.inputStream()
        repository.import(inputStream)
            .test()
            .awaitTerminalEvent()
            .assertError(NullPointerException::class.java)
    }
}
