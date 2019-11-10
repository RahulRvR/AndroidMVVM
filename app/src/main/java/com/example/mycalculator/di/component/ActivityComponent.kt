package com.example.mycalculator.di.component

import com.example.mycalculator.MainActivity
import com.example.mycalculator.di.module.ActivityModule
import com.example.mycalculator.di.module.FragmentModule
import dagger.Subcomponent

@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

    fun inject(mainActivity: MainActivity)

}
