/*
 * Created by popalay on 30.12.17 22:24
 * Copyright (c) 2017. All right reserved.
 *
 * Last modified 30.12.17 22:23
 */

package com.popalay.cardme.domain.usecase

import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.repository.CardRepository
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxkotlin.cast
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SaveCardUseCase @Inject constructor(
    private val cardRepository: CardRepository
) : UseCase<SaveCardAction> {

    override fun apply(upstream: Observable<SaveCardAction>): ObservableSource<Result> =
        upstream.switchMap {
            cardRepository.save(it.card.copy(isPending = false))
                .toSingleDefault(SaveCardResult.Success)
                .toObservable()
                .cast<SaveCardResult>()
                .onErrorReturn(SaveCardResult::Failure)
                .startWith(SaveCardResult.Idle)
                .subscribeOn(Schedulers.io())
        }
}

data class SaveCardAction(val card: Card) : Action

sealed class SaveCardResult : Result {
    object Success : SaveCardResult()
    object Idle : SaveCardResult()
    data class Failure(val throwable: Throwable) : SaveCardResult()
}