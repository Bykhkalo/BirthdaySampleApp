package com.birthdayapp.sample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.birthdayapp.sample.data.model.Profile
import com.birthdayapp.sample.domain.profile.usecase.GetProfileUseCase
import com.birthdayapp.sample.domain.profile.usecase.UpdateProfileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class DetailsViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> get() = _state

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
            getProfileUseCase.invoke().unfoldProfile()
        }
    }

    fun setBabyName(name: String) = launch {
        val currentProfile = (state.value as? State.Content)?.profile ?: return@launch
        if (currentProfile.name == name) return@launch

        val updatedProfile = currentProfile.copy(
            name = name
        )

        updateProfileUseCase.invoke(updatedProfile).unfoldProfile()
    }

    fun setBabyBirthday(timestamp: Long) = launch {
        val currentProfile = (state.value as? State.Content)?.profile ?: return@launch
        val updatedProfile = currentProfile.copy(
            birthDate = timestamp
        )

        updateProfileUseCase.invoke(updatedProfile).unfoldProfile()
    }

    private fun Result<Profile>.unfoldProfile(): Unit =
        fold(
            onSuccess = {
                _state.value = State.Content(it)
            },
            onFailure = {
                _state.value = State.LoadingError
            }
        )

    fun getFormattedBirthDate(timestamp: Long): String? {
        return try {
            val sdf = SimpleDateFormat(BIRTH_DATE_FORMAT_PATTERN, Locale.getDefault())
            val netDate = Date(timestamp)
            return sdf.format(netDate)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val BIRTH_DATE_FORMAT_PATTERN = "dd/MM/yyyy"
    }
}
