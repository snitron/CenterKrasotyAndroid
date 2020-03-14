package com.nitronapps.centerkrasoty.ui.login.presenter

import android.content.Context
import android.util.Log
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.login.interactor.LoginInteractor
import com.nitronapps.centerkrasoty.ui.login.interactor.LoginInteractorInterface
import com.nitronapps.centerkrasoty.ui.login.view.LoginActivity
import com.nitronapps.centerkrasoty.ui.login.view.LoginStatus
import com.nitronapps.centerkrasoty.ui.login.view.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter

class LoginPresenter(val context: Context) : MvpPresenter<LoginView>() {
    private var condition = LoginStatus.LOGIN
    private lateinit var interactor: LoginInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = LoginInteractor(this)
    }

    fun setRegistration() {
        condition = LoginStatus.REGISTRATION
        viewState.setStatus(condition)
    }

    private fun setLogin() {
        condition = LoginStatus.LOGIN
        viewState.setStatus(condition)
    }

    fun loginClicked(login: String, password: String) {
        interactor.login(login, password)
    }

    fun registrationClicked(
        login: String,
        password: String,
        name: String,
        surname: String,
        phone: String
    ) {
        interactor.register(
            login,
            password,
            name,
            surname,
            phone.replace(" ", "")
                .replace("+", "")
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
        )
    }

    fun getCondition(): LoginStatus {
        return condition
    }

    fun sayError(type: LoginStatus, code: Int) {
        when (type) {
            LoginStatus.LOGIN -> {
                if (code == 401)
                    viewState.sayError(context.getString(R.string.incorrectData))
                else
                    viewState.sayError(context.getString(R.string.serverError))

                setLogin()
            }

            LoginStatus.REGISTRATION -> {
                if (code == 400)
                    viewState.sayError(context.getString(R.string.loginAlreadyExists))
                else if (code == 403)
                    viewState.sayError(context.getString(R.string.phoneAlreadyExists))
                else
                    viewState.sayError(context.getString(R.string.serverError))

                setRegistration()
            }

            LoginStatus.SMS_VERIFICATION -> {
                if (code == 204)
                    viewState.sayError(context.getString(R.string.incorrectSMS))
                else
                    viewState.sayError(context.getString(R.string.serverError))
            }
        }
    }

    fun startMain() {
        viewState.startMainActivity()
    }

    fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun startSMSPage() {
        condition = LoginStatus.SMS_VERIFICATION
        viewState.setStatus(LoginStatus.SMS_VERIFICATION)
    }

    fun smsClicked(code: String) {
        interactor.sms(code.replace(" ", "").toInt())
    }
}