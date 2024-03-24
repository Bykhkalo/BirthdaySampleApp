package com.birthdayapp.sample.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val IO_DISPATCHER = "IO_DISPATCHER"

val coroutineModule = module {
    single(named(IO_DISPATCHER)) { Dispatchers.IO }
}
