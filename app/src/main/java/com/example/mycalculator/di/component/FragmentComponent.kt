package com.example.mycalculator.di.component

import com.example.mycalculator.di.module.FragmentModule
import com.example.mycalculator.ui.main.MainFragment
import dagger.Subcomponent

@Subcomponent(modules = [(FragmentModule::class)])
interface FragmentComponent {

    fun inject(mainFragment: MainFragment)
}