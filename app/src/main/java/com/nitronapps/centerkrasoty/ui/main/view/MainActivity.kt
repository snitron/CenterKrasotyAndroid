package com.nitronapps.centerkrasoty.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.login.view.LoginActivity
import com.nitronapps.centerkrasoty.ui.main.presenter.MainPresenter
import moxy.InjectViewState
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startLoginPage()
}

class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainView {
    private val presenter by moxyPresenter { MainPresenter(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun startLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
    }


}
