package ru.cherryperry.amiami.domain

import rx.Single

abstract class SingleUseCase<in Params, Result> {

    abstract fun run(param: Params): Single<Result>
}
