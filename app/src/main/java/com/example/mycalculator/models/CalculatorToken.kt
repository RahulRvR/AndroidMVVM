package com.example.mycalculator.models

enum class TokenType(val type: String) {
    NUMBER("number"),
    OPERATOR("operator")
}

data class CalculatorToken(val type:String, val value:String)

data class CalculatorNumberToken(val type:String, val value:Int)