package ru.cherryperry.amiami.data

import dagger.Binds
import dagger.Module
import ru.cherryperry.amiami.data.export.HighlightExportRepositoryImpl
import ru.cherryperry.amiami.data.network.NetworkModule
import ru.cherryperry.amiami.data.repository.CurrencyRepositoryImpl
import ru.cherryperry.amiami.data.repository.FilterRepositoryImpl
import ru.cherryperry.amiami.data.repository.HighlightRepositoryImpl
import ru.cherryperry.amiami.data.repository.ItemRepositoryImpl
import ru.cherryperry.amiami.data.repository.PushNotificationServiceImpl
import ru.cherryperry.amiami.data.repository.UpdateRepositoryImpl
import ru.cherryperry.amiami.domain.repository.CurrencyRepository
import ru.cherryperry.amiami.domain.repository.FilterRepository
import ru.cherryperry.amiami.domain.repository.HighlightExportRepository
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import ru.cherryperry.amiami.domain.repository.ItemRepository
import ru.cherryperry.amiami.domain.repository.PushNotificationService
import ru.cherryperry.amiami.domain.repository.UpdateRepository

@Module(includes = [NetworkModule::class])
abstract class DataModule {

    @Binds
    abstract fun filterRepository(filterRepositoryImpl: FilterRepositoryImpl): FilterRepository

    @Binds
    abstract fun highlightRepository(highlightRepositoryImpl: HighlightRepositoryImpl): HighlightRepository

    @Binds
    abstract fun currencyRepository(currencyRepositoryImpl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    abstract fun itemRepository(itemRepositoryImpl: ItemRepositoryImpl): ItemRepository

    @Binds
    abstract fun updateRepository(updateRepositoryImpl: UpdateRepositoryImpl): UpdateRepository

    @Binds
    abstract fun pushNotificationService(
        pushNotificationServiceImpl: PushNotificationServiceImpl
    ): PushNotificationService

    @Binds
    abstract fun highlightExportRepository(
        highlightExportRepositoryImpl: HighlightExportRepositoryImpl
    ): HighlightExportRepository
}
