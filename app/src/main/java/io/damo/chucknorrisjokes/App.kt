package io.damo.chucknorrisjokes

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment

class App : Application() {
    lateinit var serviceLocator: ServiceLocator

    override fun onCreate() {
        super.onCreate()
        serviceLocator = ServiceLocator(this)
    }
}

val Activity.app: App
    get() = application as App

val Activity.serviceLocator: ServiceLocator
    get() = app.serviceLocator

val Fragment.serviceLocator: ServiceLocator
    get() = activity.serviceLocator
