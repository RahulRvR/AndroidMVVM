package com.example.mycalculator.models

import com.squareup.moshi.Json

data class CalculatorIdResponse(@field:Json(name = "id") val id: String)