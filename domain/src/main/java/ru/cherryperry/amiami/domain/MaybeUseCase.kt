package ru.cherryperry.amiami.domain

import io.reactivex.Maybe

abstract class MaybeUseCase<in Params, Result> {

    abstract fun run(param: Params): Maybe<Result>
}
