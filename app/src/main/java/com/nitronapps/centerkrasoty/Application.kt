package com.nitronapps.centerkrasoty

import io.reactivex.plugins.RxJavaPlugins

class Application: android.app.Application() {
    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> } // nothing or some logging
    }
}