package com.example.mycalculator.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycalculator.models.CalculatorIdResponse
import com.example.mycalculator.models.CalculatorToken
import com.example.mycalculator.models.TokenType
import com.example.mycalculator.webservice.CalculatorApi
import com.example.mycalculator.webservice.CalculatorApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val retrofitService: CalculatorApiService = CalculatorApi.retrofitService) : ViewModel() {
    val stringBuilder: StringBuilder = StringBuilder()
    var calculationId: String? = null
    val result = MutableLiveData<String>()
    var lastTokenType: TokenType = TokenType.NUMBER

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun initializeCalculator() {
        compositeDisposable.addAll(
            retrofitService.getCalculationId()
                .subscribeOn(Schedulers.io())
                .map { response -> response.body() }
                .map { t: CalculatorIdResponse -> t.id }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ calculationId ->
                    this.calculationId = calculationId
                }, { throwable -> handleError(throwable) })
        )
    }

    fun postToken(token: TokenType, value: String) {
        calculationId?.let {
            retrofitService.addTokenToCalculation(it, CalculatorToken(value, token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { if (lastTokenType == TokenType.NUMBER) getResult() }
                .subscribe()
        }
    }

    fun getResult() {
        calculationId?.let {
            retrofitService.getCalculationResult(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { response -> response.body() }
                .subscribe({ response ->
                    updateResult(response!!.result)
                }, { throwable -> handleError(throwable) })
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is UndeliverableException -> {
            }
            else -> {
                // TODO handle error condition
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun updateResult(output: String) {
        stringBuilder.clear()
        stringBuilder.append(result)
        result.value = output
    }

}
