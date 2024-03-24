package com.birthdayapp.sample.data.repository

import com.birthdayapp.sample.data.model.Profile

interface ProfileRepository {

    suspend fun get(): Result<Profile>

    suspend fun update(profile: Profile): Result<Profile>

    suspend fun setName(name: String): Result<Profile>

    suspend fun setBirthDate(date: Long): Result<Profile>

    suspend fun setPhotoPath(path: String): Result<Profile>
}