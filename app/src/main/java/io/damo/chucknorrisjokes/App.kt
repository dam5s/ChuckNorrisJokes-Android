package io.damo.chucknorrisjokes

import android.app.Activity
import android.app.Application

class App : Application() {
    var serviceLocator = ServiceLocator(this)
}

val Activity.app: App
    get() = application as App

val Activity.serviceLocator: ServiceLocator
    get() = app.serviceLocator
