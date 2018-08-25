package ru.cherryperry.amiami.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import ru.cherryperry.amiami.data.db.DatabaseScheduler
import ru.cherryperry.amiami.data.db.DbHighlightRule
import ru.cherryperry.amiami.data.db.HighlightRuleDao
import ru.cherryperry.amiami.data.mapping.DbRuleToRuleMapping
import ru.cherryperry.amiami.data.mapping.RuleToDbRuleMapping
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighlightRepositoryImpl @Inject constructor(
    private val highlightRuleDao: HighlightRuleDao,
    private val appPrefs: AppPrefs,
    @DatabaseScheduler private val scheduler: Scheduler
) : HighlightRepository {

    private val dbRuleToRuleMapping = DbRuleToRuleMapping()
    private val ruleToDbRuleMapping = RuleToDbRuleMapping()

    override fun configuration(): Flowable<HighlightConfiguration> =
        Flowable.combineLatest(
            highlightRuleDao.get().map { it.map { dbRuleToRuleMapping.map(it) } },
            appPrefs.favoritesOnly.observer,
            BiFunction<List<HighlightRule>, Boolean, HighlightConfiguration>
            { list, only -> HighlightConfiguration(list, only) })

    override fun add(value: HighlightRule): Completable =
        Completable.fromAction {
            val rule = ruleToDbRuleMapping.map(value)
            highlightRuleDao.insert(rule.copy(id = null))
        }.subscribeOn(scheduler)

    override fun remove(id: Long): Completable =
        Completable.fromAction {
            highlightRuleDao.delete(DbHighlightRule(id))
        }.subscribeOn(scheduler)

    override fun replace(list: List<HighlightRule>): Completable =
        Completable.fromAction {
            highlightRuleDao.replace(list.map { ruleToDbRuleMapping.map(it).copy(id = null) })
        }.subscribeOn(scheduler)
}
