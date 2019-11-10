package com.example.mycalculator.ui.viewmodel

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val stringBuilder: StringBuilder = StringBuilder()
    var calculationId: String? = null
}
