package ru.cherryperry.amiami.data.export

import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
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
        repository
            .export(
                listOf(
                    HighlightRule(0, "test1", true),
                    HighlightRule(0, "test2", false)),
                file.outputStream())
            .test()
            .await()
            .assertComplete()
            .dispose()
        Assert.assertEquals(
            """{"highlight":[{"value":"test1","regex":true},{"value":"test2","regex":false}]}""",
            file.readText())
    }

    @Test
    fun testImportCurrentVersion() {
        val file = temporaryFolder.newFile()
        file.writeText("""{"highlight":[{"value":"test1","regex":true},{"value":"test2","regex":false}]}""")
        repository
            .import(file.inputStream())
            .test()
            .await()
            .assertValue(
                listOf(
                    HighlightRule(0, "test1", true),
                    HighlightRule(0, "test2", false)))
            .dispose()
    }

    @Test
    fun testImportOldVersion() {
        val file = temporaryFolder.newFile()
        file.writeText("""{"highlight":["test1","test2"]}""")
        repository
            .import(file.inputStream())
            .test()
            .await()
            .assertValue(
                listOf(
                    HighlightRule(0, "test1", false),
                    HighlightRule(0, "test2", false)))
            .dispose()
    }

    @Test
    fun testEmptyJson() {
        val file = temporaryFolder.newFile()
        file.writeText("""{"highlight":[]}""")
        repository
            .import(file.inputStream())
            .test()
            .await()
            .assertValue(emptyList())
            .dispose()
    }

    @Test
    fun testImportEmptyFile() {
        val file = temporaryFolder.newFile()
        val inputStream = file.inputStream()
        repository
            .import(inputStream)
            .test()
            .await()
            .assertError(JsonIOException::class.java)
            .dispose()
    }

    @Test
    fun testNotJson() {
        val file = temporaryFolder.newFile()
        file.writeText("text")
        val inputStream = file.inputStream()
        repository
            .import(inputStream)
            .test()
            .await()
            .assertError(JsonParseException::class.java)
            .dispose()
    }
}
