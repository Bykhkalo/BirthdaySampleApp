package com.birthdayapp.sample.di

import com.birthdayapp.sample.domain.profile.usecase.GetProfileUseCase
import com.birthdayapp.sample.domain.profile.usecase.SetProfilePhotoPathUseCase
import com.birthdayapp.sample.domain.profile.usecase.UpdateProfileUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetProfileUseCase(get(named(IO_DISPATCHER)), get()) }
    factory { UpdateProfileUseCase(get(named(IO_DISPATCHER)), get()) }
    factory { SetProfilePhotoPathUseCase(get(named(IO_DISPATCHER)), get(), get()) }
}
