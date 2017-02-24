package ru.cherryperry.amiami.model

import com.google.firebase.crash.FirebaseCrash
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.network.ApiProvider
import rx.Observable
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(private val exchangeApi: ApiProvider,
                                             private val appPrefs: AppPrefs) {
    private var rateCacheObservable: Observable<ExchangeRate?>? = null

    fun exchangeRate(forceNetwork: Boolean): Observable<ExchangeRate?> {
        synchronized(this) {
            if (forceNetwork || rateCacheObservable == null)
                rateCacheObservable = createObservable()
            return rateCacheObservable!!
        }
    }

    private fun createObservable(): Observable<ExchangeRate?> = exchangeApi.api()
            .flatMap { it.currency() }
            .onErrorResumeNext({
                if (it !is IOException) {
                    FirebaseCrash.report(it)
                }
                val old = appPrefs.lastExchanges
                if (old.isEmpty()) Observable.just("""{"base":"JPY","date":"2016-01-01","rates":{}}""")
                else Observable.just(old)
            })
            .map(::ExchangeRate)
            .doOnError { rateCacheObservable = null }
            .cache()
}