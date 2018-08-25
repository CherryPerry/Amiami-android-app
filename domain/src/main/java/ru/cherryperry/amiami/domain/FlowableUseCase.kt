package ru.cherryperry.amiami.domain

import io.reactivex.Flowable

abstract class FlowableUseCase<in Params, Result> {

    abstract fun run(param: Params): Flowable<Result>
}
