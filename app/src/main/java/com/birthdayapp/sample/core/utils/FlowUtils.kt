package com.birthdayapp.sample.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun <T> observeFlow(
    scope: CoroutineScope,
    flow: Flow<T>,
    action: (T) -> Unit,
) {
    scope.launch {
        flow.collect { action(it) }
    }
}

fun <T> observe(
    scope: CoroutineScope,
    stateFlow: StateFlow<T>,
    action: (T) -> Unit,
) {
    scope.launch {
        stateFlow.collect { action(it) }
    }
}

fun <T> observeSharedFlow(
    scope: CoroutineScope,
    sharedFlow: SharedFlow<T>,
    action: (T) -> Unit,
) {
    scope.launch { sharedFlow.collect(action) }
}
