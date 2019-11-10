package com.example.mycalculator.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.children
import com.example.mycalculator.R
import com.example.mycalculator.models.CalculatorIdResponse
import com.example.mycalculator.webservice.RestApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var restApi: RestApi
    private var compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Could be replaced with DI
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        restApi = RestApi()
        RxJavaPlugins.setErrorHandler { e -> handleError(e) }
        initializeCalculator()
    }

    fun handleClickListener(view: View) {
        textOutput.text =
            viewModel.stringBuilder.append(getString(getTextForButton(view.id))).toString()
        updateButtonStates(isNumber(view.id))
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
        compositeDisposable.addAll(restApi.getCalculationId()
            .subscribeOn(Schedulers.io())
            .map { response -> response.body() }
            .map { t: CalculatorIdResponse -> t.id }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ calculationId ->
                handleSuccess(calculationId)
            }, { throwable -> handleError(throwable) })
        )
    }

}
