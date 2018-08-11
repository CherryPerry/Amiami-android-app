package ru.cherryperry.amiami.data.repository

import ru.cherryperry.amiami.data.network.server.ServerApi
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.domain.repository.CurrencyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val api: ServerApi,
    private val appPrefs: AppPrefs
) : CurrencyRepository {

    override fun currency() = api.currency()

    override fun selectedCurrency() = appPrefs.exchangeCurrency.observer
}
