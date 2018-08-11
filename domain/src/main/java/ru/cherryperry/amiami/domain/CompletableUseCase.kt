package ru.cherryperry.amiami.domain

import rx.Completable

abstract class CompletableUseCase<in Params> {

    abstract fun run(param: Params): Completable
}
