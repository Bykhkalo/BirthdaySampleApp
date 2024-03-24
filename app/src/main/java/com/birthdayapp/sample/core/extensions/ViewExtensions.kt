package com.birthdayapp.sample.core.extensions

import android.view.View


private const val DELAY_MILLIS = 150L

fun View.setMultiClickProtectedOnClickListener(action: (() -> Unit)?) {
    if (action == null) {
        setOnClickListener(null)
        return
    }

    val reEnableButtonRunnable = Runnable {
        isEnabled = true
    }

    setOnClickListener {
        isEnabled = false
        action.invoke()
        postDelayed(reEnableButtonRunnable, DELAY_MILLIS)
    }
}
