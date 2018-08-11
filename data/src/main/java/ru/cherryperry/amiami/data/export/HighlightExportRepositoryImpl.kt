package ru.cherryperry.amiami.data.export

import com.google.gson.Gson
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.repository.HighlightExportRepository
import rx.Completable
import rx.Single
import rx.schedulers.Schedulers
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighlightExportRepositoryImpl @Inject constructor() : HighlightExportRepository {

    private val gson = Gson()

    override fun export(rules: List<HighlightRule>, outputStream: OutputStream): Completable =
        Completable
            .fromAction {
                outputStream.bufferedWriter().use { writer ->
                    val settings = ExportedData(rules.map { it.rule })
                    gson.toJson(settings, writer)
                }
            }
            .subscribeOn(Schedulers.io())

    override fun import(inputStream: InputStream): Single<List<HighlightRule>> =
        Single
            .fromCallable {
                inputStream.reader().use { reader ->
                    val settings = gson.fromJson(reader, ExportedData::class.java)
                    settings.highlight.map { HighlightRule(it) }
                }
            }
            .subscribeOn(Schedulers.io())
}
