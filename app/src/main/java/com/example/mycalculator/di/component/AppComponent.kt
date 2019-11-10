package com.example.mycalculator.di.component

import android.app.Application
import com.example.mycalculator.di.module.ActivityModule
import com.example.mycalculator.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun inject(application: Application)

}
