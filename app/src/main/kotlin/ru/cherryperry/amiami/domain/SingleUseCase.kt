package ru.cherryperry.amiami.domain

import rx.Single

/**
 * Some logic, that produces result for presenter with provided params
 */
abstract class SingleUseCase<in Params : UseCaseParam, Result : UseCaseResult> {

    abstract fun run(param: Params): Single<Result>
}