package com.birthdayapp.sample.domain.profile.usecase

import android.net.Uri
import com.birthdayapp.sample.core.delegate.ImageFilesHandlingDelegate
import com.birthdayapp.sample.core.extensions.result.flatTransform
import com.birthdayapp.sample.data.model.Profile
import com.birthdayapp.sample.data.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SetProfilePhotoPathUseCase(
    private val dispatcher: CoroutineDispatcher,
    private val profileRepository: ProfileRepository,
    private val imageFilesHandlingDelegate: ImageFilesHandlingDelegate,
) {

    suspend operator fun invoke(photoPath: String): Result<Profile> = withContext(dispatcher) {
        profileRepository.setPhotoPath(photoPath)
    }

    suspend operator fun invoke(imageUri: Uri): Result<Profile> = withContext(dispatcher) {
        runCatching {
            imageFilesHandlingDelegate.getFileFrom(imageUri)
                ?: throw IllegalStateException("Can't get file from imageUri: $imageUri")
        }.flatTransform { file ->
            profileRepository.setPhotoPath(file.absolutePath)
        }
    }
}
