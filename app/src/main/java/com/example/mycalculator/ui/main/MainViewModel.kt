package com.example.mycalculator.ui.main

import androidx.lifecycle.ViewModel
import java.lang.StringBuilder

class MainViewModel : ViewModel() {

    val stringBuilder: StringBuilder = StringBuilder()
    var calculationId: String? = null
}
