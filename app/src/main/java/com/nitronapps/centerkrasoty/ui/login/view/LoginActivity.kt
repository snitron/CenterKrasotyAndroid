package com.nitronapps.centerkrasoty.ui.login.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.login.presenter.LoginPresenter
import com.nitronapps.centerkrasoty.ui.view.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface LoginView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setStatus(status: LoginStatus)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startMainActivity()
}

class LoginActivity : MvpAppCompatActivity(R.layout.activity_login), LoginView {
    private val presenter by moxyPresenter { LoginPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setStatus(LoginStatus.LOGIN)

        textViewRegistation.setOnClickListener {
            presenter.setRegistration()
        }

        buttonLogin.setOnClickListener {
            if(!editTextLogin.text.equals("") && !editTextPassword.equals("")){
                it.visibility = View.GONE

                progressBarLogin.visibility = View.VISIBLE
                presenter.loginClicked(editTextLogin.text.toString(), editTextPassword.text.toString())
            }
        }
    }

    override fun setStatus(status: LoginStatus){
        when (status) {
            LoginStatus.LOGIN -> {
                editTextPhone.visibility = View.GONE
                buttonLogin.text = "ВОЙТИ"
            }

            LoginStatus.REGISTRATION -> {
                editTextPhone.visibility = View.VISIBLE
                buttonLogin.text = "ЗАРЕГИСТРИРОВАТЬСЯ"
            }
        }
    }

    override fun sayError() {
        Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
    }

    override fun startMainActivity() {
        runOnUiThread {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}

enum class LoginStatus{
    LOGIN, REGISTRATION;
}
