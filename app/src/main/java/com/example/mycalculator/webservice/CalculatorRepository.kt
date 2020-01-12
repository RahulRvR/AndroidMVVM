package com.example.mycalculator.webservice

import com.example.mycalculator.models.CalculatorIdResponse
import com.example.mycalculator.models.CalculatorResultResponse
import com.example.mycalculator.models.CalculatorToken
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private lateinit var calculatorApi: CalculatorApi

private val retrofit = Retrofit.Builder()
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl("https://calculator-frontend-challenge.herokuapp.com/")
    .build()

interface CalculatorApiService {

    @POST("/calculations")
    fun getCalculationId(): Single<Response<CalculatorIdResponse>>

    @POST("/calculations/{calculationId}/tokens")
    fun addTokenToCalculation(@Path("calculationId") calculationId: String, @Body input: CalculatorToken): Single<Response<CalculatorToken>>


    @GET("/calculations/{calculationId}/result")
    fun getCalculationResult(@Path("calculationId") calculationId: String): Single<Response<CalculatorResultResponse>>
}

object CalculatorApi {

    val retrofitService: CalculatorApiService by lazy { retrofit.create(CalculatorApiService::class.java) }

}