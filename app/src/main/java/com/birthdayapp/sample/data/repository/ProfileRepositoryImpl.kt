package com.birthdayapp.sample.data.repository

import com.birthdayapp.sample.data.datasource.ProfilePreferences
import com.birthdayapp.sample.data.model.Profile
import com.birthdayapp.sample.core.extensions.result.flatTransform

class ProfileRepositoryImpl(
    private val profilePreferences: ProfilePreferences
) : ProfileRepository {

    override suspend fun get(): Result<Profile> = runCatching {
        Profile(
            name = profilePreferences.getName().orEmpty(),
            birthDate = profilePreferences.getBirthDate() ?: Profile.DEFAULT_BIRTH_DATE,
            photoPath = profilePreferences.getPhotoPath().orEmpty()
        )
    }

    override suspend fun update(profile: Profile): Result<Profile> = runCatching {
        with(profile) {
            profilePreferences.setName(name)
            profilePreferences.setBirthdate(birthDate)
            profilePreferences.setPhotoPath(photoPath)
        }
    }.flatTransform { get() }

    override suspend fun setName(name: String): Result<Profile> = runCatching {
        profilePreferences.setName(name)
        return get()
    }

    override suspend fun setBirthDate(date: Long): Result<Profile> = runCatching {
        profilePreferences.setBirthdate(date)
        return get()
    }

    override suspend fun setPhotoPath(path: String): Result<Profile> = runCatching {
        profilePreferences.setPhotoPath(path)
        return get()
    }
}
