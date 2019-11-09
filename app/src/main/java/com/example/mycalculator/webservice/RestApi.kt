package com.example.mycalculator.webservice

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private lateinit var calculatorApi:CalculatorApi

class RestApi {
    init {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://calculator-frontend-challenge.herokuapp.com/")
            .build()

        calculatorApi = retrofit.create(CalculatorApi::class.java)
    }

    fun getCalculationId() = calculatorApi.getCalculationId()
}