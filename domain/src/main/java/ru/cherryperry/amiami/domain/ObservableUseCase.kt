package ru.cherryperry.amiami.domain

import rx.Observable

abstract class ObservableUseCase<in Params, Result> {

    abstract fun run(param: Params): Observable<Result>
}
