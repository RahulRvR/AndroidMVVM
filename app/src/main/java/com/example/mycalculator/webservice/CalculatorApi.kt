package com.example.mycalculator.webservice

import com.example.mycalculator.models.CalculatorInputData
import com.example.mycalculator.models.CalculatorIdResponse
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CalculatorApi {

    @POST("/calculations")
    fun getCalculationId(): Single<Response<CalculatorIdResponse>>

    @POST("/calculations/{calculationId}/tokens")
    fun addTokenToCalculation(@Path("calculationId") calculationId: String, @Body input: CalculatorInputData): Single<JSONObject>

    @GET("/calculations/{calculationId}/tokens")
    fun getCalculationResult(@Path("calculationId") calculationId: String): Single<JSONObject>
}