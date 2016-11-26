package io.damo.chucknorrisjokes.utils

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

fun <T> observe(callable: () -> T): Observable<T> = Observable
    .fromCallable(callable)
    .subscribeOn(Schedulers.newThread())
    .observeOn(AndroidSchedulers.mainThread())
