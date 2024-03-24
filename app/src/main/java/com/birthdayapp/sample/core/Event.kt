package com.birthdayapp.sample.core

data class Event<out T>(private val content: T) {

    private var hasBeenCollected = false

    /**
     * Returns the content exactly one time.
     *
     * Any second invocation of this method will return null.
     */
    fun getOnce(): T? =
        if (hasBeenCollected) {
            null
        } else {
            hasBeenCollected = true
            content
        }

    /**
     * Returns the content, even if it's already been handled.
     *
     * Accessing the content this way still marks it as retrieved.
     */
    fun peekContent(): T = content.also {
        hasBeenCollected = true
    }
}
