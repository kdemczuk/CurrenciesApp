package com.example.currencies

import com.example.currencies.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App: DaggerApplication() {

    private val appComponent = DaggerApplicationComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
        initInjection()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

        }
    }
    private fun initInjection() = appComponent.inject(this)

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        appComponent
}