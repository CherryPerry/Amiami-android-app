package ru.cherryperry.amiami.data.export

import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.repository.HighlightExportRepository
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighlightExportRepositoryImpl @Inject constructor() : HighlightExportRepository {

    private val gson = GsonBuilder()
        .registerTypeAdapter(ExportedData::class.java, ExportedDataGsonTypeAdapter())
        .create()

    override fun export(rules: List<HighlightRule>, outputStream: OutputStream): Completable =
        Completable
            .fromAction {
                outputStream.bufferedWriter().use { writer ->
                    val settings = ExportedData(rules.map { ExportedItem(it.rule, it.regex) })
                    gson.toJson(settings, writer)
                }
            }
            .subscribeOn(Schedulers.io())

    override fun import(inputStream: InputStream): Single<List<HighlightRule>> =
        Single
            .fromCallable {
                inputStream.reader().use { reader ->
                    val settings = gson.fromJson(reader, ExportedData::class.java)
                        ?: throw JsonIOException("Empty file")
                    settings.highlight.map { HighlightRule(0, it.value, it.regex) }
                }
            }
            .subscribeOn(Schedulers.io())
}
