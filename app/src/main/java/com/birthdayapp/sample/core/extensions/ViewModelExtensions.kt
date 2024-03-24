package com.birthdayapp.sample.core.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.birthdayapp.sample.core.utils.observeSharedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Simplifies the observation of [LiveData].
 *
 * ## From:
 * ```
 * viewModel.getData().observe(this, Observer<String>{ data ->
 *      textView.text = it
 * })
 * ```
 *
 * ## To:
 * ```
 *  observe(viewModel.getData()) { textView.text = it }
 *
 *  with(viewModel) {
 *      observe(getState(), ::handleState)
 *  }
 *
 *  ...
 *
 *  private fun handleState(state: State?) {
 *      ...
 *  }
 * ```
 */

/*
 *  Observe [LiveData] with the adjusted LifeCycleOwner for Fragments to prevent leaks.
 *
 *  For more information visit:
 *  https://developer.android.com/reference/androidx/fragment/app/Fragment.html#getViewLifecycleOwner()
 *  https://proandroiddev.com/5-common-mistakes-when-using-architecture-components-403e9899f4cb [Leaking LiveData observers in Fragments]
 */
fun <T : Any> Fragment.observe(liveData: LiveData<T>, body: (T) -> Unit) {
    liveData.observe(this.viewLifecycleOwner, Observer(body))
}

/*
 * Works like observe but recognizes changes to null as well
 */
fun <T : Any> Fragment.observeGetEmpty(liveData: LiveData<T?>, body: (T?) -> Unit) {
    liveData.observe(this.viewLifecycleOwner, Observer(body))
}

/*
 * Same as [Fragment.observe] but the argument in the [body] argument function
 * is never null even though in the source livedata it can be null.
 */
fun <T : Any?> Fragment.safeObserve(liveData: LiveData<T?>, body: (T) -> Unit) {
    liveData.observe(this.viewLifecycleOwner) { it?.let(body) }
}

fun <T : Any> Fragment.observeLiveEvent(liveEvent: LiveEvent<T>, f: (T) -> Unit) {
    liveEvent.observe(this.viewLifecycleOwner) { it.getOnce()?.let(f) }
}

fun <T : Any> Fragment.safeObserveLiveEvent(liveEvent: LiveEvent<T>, f: (T) -> Unit) {
    liveEvent.observe(this.viewLifecycleOwner) { it?.getOnce()?.let(f) }
}

fun <T> Fragment.observeFlow(flow: Flow<T>, collector: (T) -> Unit) {
    com.birthdayapp.sample.core.utils.observeFlow(
        scope = viewLifecycleOwner.lifecycleScope,
        flow = flow,
        action = collector
    )
}

fun <T> Fragment.observe(stateFlow: StateFlow<T>, collector: (T) -> Unit) {
    com.birthdayapp.sample.core.utils.observe(
        scope = viewLifecycleOwner.lifecycleScope,
        stateFlow = stateFlow,
        action = collector,
    )
}

fun <T> Fragment.observe(sharedFlow: SharedFlow<T>, collector: (T) -> Unit) {
    observeSharedFlow(
        scope = viewLifecycleOwner.lifecycleScope,
        sharedFlow = sharedFlow,
        action = collector,
    )
}

fun <T> Fragment.observeSharedFlow(sharedFlow: SharedFlow<T>, collector: (T) -> Unit) {
    observeSharedFlow(
        scope = viewLifecycleOwner.lifecycleScope,
        sharedFlow = sharedFlow,
        action = collector,
    )
}

fun <T : Any> AppCompatActivity.observeLiveEvent(liveEvent: LiveEvent<T>, f: (T) -> Unit) {
    liveEvent.observe(this) { it?.getOnce()?.let(f) }
}

fun <T : Any> AppCompatActivity.observe(liveData: LiveData<T>, body: (T) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun <T> AppCompatActivity.observe(sharedFlow: SharedFlow<T>, collector: (T) -> Unit) {
    observeSharedFlow(
        scope = lifecycleScope,
        sharedFlow = sharedFlow,
        action = collector,
    )
}

/*
 * Same as [AppCompatActivity.observe] but the argument in the [body] argument function
 * is never null even though in the source livedata it can be null.
 */
fun <T : Any?> AppCompatActivity.safeObserve(liveData: LiveData<T?>, body: (T) -> Unit) {
    liveData.observe(this) { it?.let(body) }
}

fun <T : Any> AppCompatActivity.safeObserveEvent(liveEvent: LiveEvent<T>, body: (T) -> Unit) {
    liveEvent.observe(this) { it?.getOnce()?.let(body) }
}
