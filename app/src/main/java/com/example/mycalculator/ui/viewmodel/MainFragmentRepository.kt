package com.example.mycalculator.ui.viewmodel

import com.example.mycalculator.models.CalculatorIdResponse
import com.example.mycalculator.models.TokenType
import com.example.mycalculator.webservice.CalculatorRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.schedulers.Schedulers


private val calculatorRepository: CalculatorRepository =
    CalculatorRepository()
private var compositeDisposable = CompositeDisposable()

class MainFragmentRepository(private val viewModel: MainViewModel) {

    fun initializeCalculator() {
        compositeDisposable.addAll(
            calculatorRepository.getCalculationId()
            .subscribeOn(Schedulers.io())
            .map { response -> response.body() }
            .map { t: CalculatorIdResponse -> t.id }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ calculationId ->
                viewModel.calculationId = calculationId
            }, { throwable -> handleError(throwable) })
        )
    }

    fun postToken(inputValue: String) {
        if(viewModel.calculationId!=null) {
            compositeDisposable.addAll(viewModel.calculationId?.let {
                calculatorRepository.addTokenToCalculation(it, viewModel.lastTokenType, inputValue)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { if (viewModel.lastTokenType == TokenType.NUMBER) getResult() }
                    .subscribe()
            })
        }
    }

    fun getResult() {
        if(viewModel.calculationId!=null) {
            compositeDisposable.addAll(viewModel.calculationId?.let {
                calculatorRepository.getCalculationResult(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { response -> response.body() }
                    .subscribe({ response ->
                        updateResult(response!!.result)
                    }, { throwable -> handleError(throwable) })
            })
        }
    }

    fun clear() {
        compositeDisposable.clear()
    }

    private fun updateResult(result: String) {
        viewModel.stringBuilder.clear()
        viewModel.stringBuilder.append(result)
        viewModel.result.value = result
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

}