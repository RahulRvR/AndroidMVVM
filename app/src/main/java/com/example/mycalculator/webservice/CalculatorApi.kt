package com.example.mycalculator.webservice

import com.example.mycalculator.models.CalculatorIdResponse
import com.example.mycalculator.models.CalculatorNumberToken
import com.example.mycalculator.models.CalculatorResultResponse
import com.example.mycalculator.models.CalculatorToken
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CalculatorApi {

    @POST("/calculations")
    fun getCalculationId(): Single<Response<CalculatorIdResponse>>

    @POST("/calculations/{calculationId}/tokens")
    fun addTokenToCalculation(@Path("calculationId") calculationId: String, @Body input: CalculatorToken): Single<Response<CalculatorToken>>

    @POST("/calculations/{calculationId}/tokens")
    fun addTokenToCalculation(@Path("calculationId") calculationId: String, @Body input: CalculatorNumberToken): Single<Response<CalculatorToken>>

    @GET("/calculations/{calculationId}/result")
    fun getCalculationResult(@Path("calculationId") calculationId: String): Single<Response<CalculatorResultResponse>>
}