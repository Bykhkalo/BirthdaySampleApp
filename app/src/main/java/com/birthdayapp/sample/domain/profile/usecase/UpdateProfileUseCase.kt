package com.birthdayapp.sample.domain.profile.usecase

import com.birthdayapp.sample.data.model.Profile
import com.birthdayapp.sample.data.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateProfileUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val profileRepository: ProfileRepository,
) {

    suspend operator fun invoke(profile: Profile): Result<Profile> = withContext(dispatcher) {
        profileRepository.update(profile)
    }
}
