package com.birthdayapp.sample.core.extensions

import androidx.lifecycle.*
import com.birthdayapp.sample.core.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>
typealias MediatorLiveEvent<T> = MediatorLiveData<Event<T>>

/**
 * Extends a [MutableLiveEvent] initialized with the given {@code value}.
 *
 * @param value initial value
 */
fun <T> MutableLiveEvent(value: T): MutableLiveData<Event<T>> = MutableLiveData(Event(value))

fun <T> MutableLiveEvent<T>.post(event: T) {
    value = Event(event)
}

inline fun <T> LiveData<T>.filter(crossinline predicate: (T) -> Boolean): LiveData<T> {
    val mutableLiveData: MediatorLiveData<T> = MediatorLiveData()
    mutableLiveData.addSource(this) {
        if (predicate(it)) {
            mutableLiveData.value = it
        }
    }
    return mutableLiveData
}

/**
 * Forwards value changes from a [LiveEvent] instance to a target [MediatorLiveEvent].
 */
fun <T> LiveEvent<T>.forwardEvent(target: MediatorLiveEvent<T>): Unit {
    target.addSource(this, target::setValue)
}