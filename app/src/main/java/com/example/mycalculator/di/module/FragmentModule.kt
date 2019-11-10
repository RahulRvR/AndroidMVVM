package com.example.mycalculator.di.module

import androidx.lifecycle.ViewModelProviders
import com.example.mycalculator.ui.main.MainFragment
import com.example.mycalculator.ui.viewmodel.MainViewModel
import com.example.mycalculator.webservice.CalculatorRepository
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: MainFragment) {

    @Provides
    fun provideMainViewModel(): MainViewModel =
        ViewModelProviders.of(fragment).get(MainViewModel::class.java)

    @Provides
    fun provideRepository():CalculatorRepository = CalculatorRepository()
}
