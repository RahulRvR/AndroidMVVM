package com.example.mycalculator.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycalculator.models.TokenType

class MainViewModel : ViewModel() {

    val stringBuilder: StringBuilder = StringBuilder()
    var calculationId: String? = null
    val result = MutableLiveData<String>()
    var lastTokenType: TokenType = TokenType.NUMBER
}
