package com.birthdayapp.sample.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.birthdayapp.sample.R
import com.birthdayapp.sample.core.delegate.ImageFilesHandlingDelegate
import com.birthdayapp.sample.data.model.Profile
import com.birthdayapp.sample.domain.profile.usecase.GetProfileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BirthdayViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val imageFilesHandlingDelegate: ImageFilesHandlingDelegate,
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

    private val digitDrawableResIds: Array<Int> = arrayOf(
        R.drawable.ic_zero,
        R.drawable.ic_one,
        R.drawable.ic_two,
        R.drawable.ic_three,
        R.drawable.ic_four,
        R.drawable.ic_five,
        R.drawable.ic_six,
        R.drawable.ic_seven,
        R.drawable.ic_eight,
        R.drawable.ic_nine,
    )

    sealed class State {
        data object Loading : State()
        data object LoadingError : State()
        data class Content(val profile: Profile) : State()
    }

    init {
        loadContent()
    }

    fun loadContent() {
        launch {
            _state.value = State.Loading
            getProfileUseCase.invoke().fold(
                onSuccess = {
                    _state.value = State.Content(it)
                },
                onFailure = {
                    _state.value = State.LoadingError
                }
            )
        }
    }

    fun getDigitDrawableResId(digit: Int): Int = digitDrawableResIds[digit]

    fun saveScreenshot(bitmap: Bitmap): Uri? {
        return try {
            imageFilesHandlingDelegate.saveScreenshot(bitmap)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val MAX_SUPPORTED_AGE_DISPLAY_VALUE = 99
        const val MIN_SUPPORTED_AGE_DISPLAY_VALUE = 0
        const val DIGIT_MAX_VALUE = 9
        const val DIGIT_MIN_VALUE = 0
    }
}
