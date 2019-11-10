package com.example.mycalculator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.mycalculator.MainActivity
import com.example.mycalculator.R
import com.example.mycalculator.di.module.FragmentModule
import com.example.mycalculator.models.CalculatorIdResponse
import com.example.mycalculator.models.TokenType
import com.example.mycalculator.ui.viewmodel.MainViewModel
import com.example.mycalculator.webservice.CalculatorRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModel: MainViewModel
    @Inject
    lateinit var calculatorRepository: CalculatorRepository

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity)
            .activityComponent
            .fragmentComponent(FragmentModule(this)).inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        RxJavaPlugins.setErrorHandler { e -> handleError(e) }
        initializeCalculator()
        buttonEquals.setOnClickListener {getResult() }
    }

    fun handleClickListener(view: View) {
        val inputValue = getString(getTextForButton(view.id))
        textOutput.text = viewModel.stringBuilder.append(inputValue).toString()
        val isNumber = isNumber(view.id)
        updateButtonStates(isNumber)
        if(viewModel.calculationId != null) {
            postToken(
                viewModel.calculationId!!,
                if (isNumber) TokenType.NUMBER else TokenType.OPERATOR,
                inputValue
            )
        }
    }

    private fun getTextForButton(id: Int) = when (id) {
        R.id.button1 -> R.string.number_1
        R.id.button2 -> R.string.number_2
        R.id.button3 -> R.string.number_3
        R.id.button4 -> R.string.number_4
        R.id.button5 -> R.string.number_5
        R.id.button6 -> R.string.number_6
        R.id.button7 -> R.string.number_7
        R.id.button8 -> R.string.number_8
        R.id.button9 -> R.string.number_9
        R.id.buttonPlus -> R.string.plus
        R.id.buttonMinus -> R.string.minus
        else -> R.string.number_0
    }

    private fun isNumber(id: Int) = when (id) {
        R.id.button1, R.id.button2, R.id.button3,
        R.id.button4, R.id.button5, R.id.button6,
        R.id.button7, R.id.button8, R.id.button9 -> true
        else -> false
    }

    private fun updateButtonStates(isNumber: Boolean) {

        row1.children.forEach { view ->
            if (view is Button) {
                view.isEnabled = !isNumber
                view.isClickable = !isNumber
                if (isNumber) {
                    view.alpha = 0.2f
                } else {
                    view.alpha = 1.0f
                }
            }
        }

        row2.children.forEach { view ->
            view.isEnabled = !isNumber
            view.isClickable = !isNumber
            if (isNumber) {
                view.alpha = 0.2f
            } else {
                view.alpha = 1.0f
            }
        }

        row3.children.forEach { view ->
            view.isEnabled = !isNumber
            view.isClickable = !isNumber
            if (isNumber) {
                view.alpha = 0.2f
            } else {
                view.alpha = 1.0f
            }
        }

        row4.children.forEach { view ->
            view.isEnabled = isNumber
            view.isClickable = isNumber
            if (!isNumber) {
                view.alpha = 0.2f
            } else {
                view.alpha = 1.0f
            }
        }

    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    private fun handleSuccess(id: String) {
        viewModel.calculationId = id
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is UndeliverableException -> {
            }
            else -> {
                Toast.makeText(context, throwable.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeCalculator() {
        compositeDisposable.addAll(calculatorRepository.getCalculationId()
            .subscribeOn(Schedulers.io())
            .map { response -> response.body() }
            .map { t: CalculatorIdResponse -> t.id }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ calculationId ->
                handleSuccess(calculationId)
            }, { throwable -> handleError(throwable) })
        )
    }

    private fun postToken(id:String, tokenType: TokenType, value:String) {
        compositeDisposable.addAll(calculatorRepository.addTokenToCalculation(id, tokenType,value)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {if(tokenType == TokenType.NUMBER) getResult()}
            .subscribe()
        )
    }

    private fun updateResult(result:String) {
        viewModel.stringBuilder.clear()
        viewModel.stringBuilder.append(result)
        textOutput.text = viewModel.stringBuilder.toString()
    }


    private fun getResult() {
        compositeDisposable.addAll(viewModel.calculationId?.let {
            calculatorRepository.getCalculationResult(it)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { response -> response.body() }
                .subscribe({ response ->
                    updateResult(response!!.result)
                }, { throwable -> handleError(throwable) })
        }
        )
    }


}
