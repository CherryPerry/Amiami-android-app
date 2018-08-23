package ru.cherryperry.amiami.data.repository

import android.arch.persistence.room.Room
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import ru.cherryperry.amiami.data.db.AppDatabase
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.HighlightRule

@RunWith(RobolectricTestRunner::class)
class HighlightRepositoryImplTest {

    private lateinit var appPrefs: AppPrefs
    private lateinit var appDatabase: AppDatabase
    private lateinit var highlightRepositoryImpl: HighlightRepositoryImpl

    @Before
    fun before() {
        appPrefs = AppPrefs(RuntimeEnvironment.application)
        appDatabase = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application, AppDatabase::class.java).build()
        highlightRepositoryImpl = HighlightRepositoryImpl(appDatabase.highlightRuleDao(), appPrefs)
    }

    @After
    fun after() {
        appDatabase.close()
    }

    @Test
    fun testEmptyGet() {
        appPrefs.favoritesOnly.value = false
        // empty db should return empty list
        highlightRepositoryImpl.configuration()
            .test()
            .awaitCount(1)
            .assertValue(HighlightConfiguration(emptyList()))
    }

    @Test
    fun testFavoritesOnlyParameter() {
        appPrefs.favoritesOnly.value = true
        // configuration should include correct asFilter value
        highlightRepositoryImpl.configuration()
            .test()
            .awaitCount(1)
            .assertValue(HighlightConfiguration(emptyList(), true))
    }

    @Test
    fun testFavoritesOnlyChanged() {
        appPrefs.favoritesOnly.value = false
        val subscriber = highlightRepositoryImpl.configuration()
            .test()
            .awaitCount(1)
        // favoritesOnly change should notify about it
        appPrefs.favoritesOnly.value = true
        subscriber
            .awaitCount(2)
            .assertValues(
                HighlightConfiguration(),
                HighlightConfiguration(asFilter = true))
            .assertNotComplete()
            .assertNoErrors()
    }

    @Test
    fun testInsertItem() {
        appPrefs.favoritesOnly.value = false
        val item1 = HighlightRule(1, "test1", true)
        val subscriber = highlightRepositoryImpl.configuration().test().awaitCount(1)
        highlightRepositoryImpl.add(item1).test().await().assertComplete()
        // item insertion should notify subscriber
        subscriber
            .awaitCount(2)
            .assertValues(
                HighlightConfiguration(),
                HighlightConfiguration(listOf(item1)))
            .assertNotComplete()
            .assertNoErrors()
    }

    @Test
    fun testDeleteItem() {
        appPrefs.favoritesOnly.value = false
        val item1 = HighlightRule(1, "test1", true)
        val item2 = HighlightRule(2, "test2", false)
        // insert data
        highlightRepositoryImpl.add(item1).test().await().assertComplete()
        highlightRepositoryImpl.add(item2).test().await().assertComplete()
        val subscriber = highlightRepositoryImpl.configuration().test().awaitCount(1)
        // remove one
        highlightRepositoryImpl.remove(1).test().await().assertComplete()
        // item removal should notify subscriber
        subscriber
            .awaitCount(2)
            .assertValues(
                HighlightConfiguration(listOf(item1, item2)),
                HighlightConfiguration(listOf(item2)))
            .assertNotComplete()
            .assertNoErrors()
    }

    @Test
    fun testInsertMultipleItem() {
        appPrefs.favoritesOnly.value = false
        val item1 = HighlightRule(1, "test1", true)
        val item2 = HighlightRule(2, "test2", false)
        highlightRepositoryImpl.add(item1).test().await().assertComplete()
        val subscriber = highlightRepositoryImpl.configuration()
            .test()
            .awaitCount(1)
        // item insertions should not drop table
        highlightRepositoryImpl.add(item2).test().await().assertComplete()
        subscriber
            .awaitCount(2)
            .assertValues(
                HighlightConfiguration(listOf(item1)),
                HighlightConfiguration(listOf(item1, item2)))
            .assertNotComplete()
            .assertNoErrors()
    }

    // TODO TEST REPLACE METHOD
}