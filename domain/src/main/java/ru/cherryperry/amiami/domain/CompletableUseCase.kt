package ru.cherryperry.amiami.domain

import io.reactivex.Completable

abstract class CompletableUseCase<in Params> {

    abstract fun run(param: Params): Completable
}
