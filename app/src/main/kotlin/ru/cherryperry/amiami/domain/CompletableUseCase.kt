package ru.cherryperry.amiami.domain

import rx.Completable

/**
 * Some logic, that produces result for presenter with provided params
 */
abstract class CompletableUseCase<in Params> {

    abstract fun run(param: Params): Completable
}