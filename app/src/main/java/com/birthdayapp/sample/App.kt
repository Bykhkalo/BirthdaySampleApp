package com.birthdayapp.sample

import android.app.Application
import com.birthdayapp.sample.di.coroutineModule
import com.birthdayapp.sample.di.mainModule
import com.birthdayapp.sample.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDi()
    }

    private fun setupDi() {
        startKoin {
            androidContext(this@App)
            modules(
                mainModule,
                coroutineModule,
                useCaseModule,
            )
        }
    }
}
