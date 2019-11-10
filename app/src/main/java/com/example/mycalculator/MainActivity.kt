package com.example.mycalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mycalculator.app.MyApplication
import com.example.mycalculator.di.component.ActivityComponent
import com.example.mycalculator.di.module.ActivityModule
import com.example.mycalculator.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var fragment: MainFragment

    val activityComponent: ActivityComponent by lazy {
        MyApplication()
            .component
            .activityComponent(ActivityModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        fragment = MainFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()
        }
    }

    fun handleClickListener(view: View) {
        fragment.handleClickListener(view)
    }

}
