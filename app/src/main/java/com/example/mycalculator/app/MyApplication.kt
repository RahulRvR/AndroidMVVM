package com.example.mycalculator.app

import android.app.Application
import com.example.mycalculator.di.component.AppComponent
import com.example.mycalculator.di.component.DaggerAppComponent
import com.example.mycalculator.di.module.AppModule

class MyApplication : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}