package com.birthdayapp.sample.di

import com.birthdayapp.sample.core.delegate.ImageFilesHandlingDelegate
import com.birthdayapp.sample.data.datasource.ProfilePreferences
import com.birthdayapp.sample.data.repository.ProfileRepository
import com.birthdayapp.sample.data.repository.ProfileRepositoryImpl
import com.birthdayapp.sample.viewmodel.DetailsViewModel
import com.birthdayapp.sample.viewmodel.ImagePickerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {

    viewModelOf(::DetailsViewModel)
    viewModelOf(::ImagePickerViewModel)

    factory { ProfilePreferences(androidContext()) }
    factory<ProfileRepository> { ProfileRepositoryImpl(get()) }

    factory { ImageFilesHandlingDelegate(androidContext()) }
}
