package com.example.mycalculator.webservice

import com.example.mycalculator.models.CalculatorNumberToken
import com.example.mycalculator.models.CalculatorToken
import com.example.mycalculator.models.TokenType
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private lateinit var calculatorApi: CalculatorApi

class CalculatorRepository() {
    init {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://calculator-frontend-challenge.herokuapp.com/")
            .build()

        calculatorApi = retrofit.create(CalculatorApi::class.java)
    }

    fun getCalculationId() = calculatorApi.getCalculationId()

    fun addTokenToCalculation(id: String, token: TokenType, value: String) =
        when (token) {
            TokenType.NUMBER -> {
                calculatorApi.addTokenToCalculation(
                    id,
                    CalculatorNumberToken(type = token.type, value = value.toInt())
                )
            }
            else -> {
                calculatorApi.addTokenToCalculation(
                    id,
                    CalculatorToken(type = token.type, value = value)
                )
            }
        }

    fun getCalculationResult(id:String) = calculatorApi.getCalculationResult(id)
}