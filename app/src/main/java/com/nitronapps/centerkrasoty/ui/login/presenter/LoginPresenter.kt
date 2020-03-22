package com.nitronapps.centerkrasoty.ui.login.presenter

import android.content.Context
import android.util.Log
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.model.UserLogin
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
    private var attempts = 0
    private var userLogin: UserLogin? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = LoginInteractor(this)

    }

    fun setRegistration() {
        condition = LoginStatus.REGISTRATION
        viewState.setStatus(condition)
    }

    fun setLogin() {
        condition = LoginStatus.LOGIN
        viewState.setStatus(condition)
    }

    fun loginClicked(login: String, password: String) {
        interactor.login(login, password)
        viewState.setProgressBarEnabled(true)
        viewState.setButtonEnabled(false)
    }

    fun registrationClicked(
        password: String,
        name: String,
        surname: String,
        phone: String
    ) {
        userLogin = UserLogin(
            password = password,
            name = name,
            surname = surname,
            phone = phone
        )

        interactor.getNewSms(phone, true)
        viewState.setProgressBarEnabled(true)
        viewState.setButtonEnabled(false)
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
                when (code) {
                    400 -> viewState.sayError(context.getString(R.string.loginAlreadyExists))
                    403 -> viewState.sayError(context.getString(R.string.phoneAlreadyExists))
                    else -> viewState.sayError(context.getString(R.string.serverError))
                }

                setRegistration()
            }

            LoginStatus.SMS_VERIFICATION -> {
                when (code) {
                    401 -> viewState.sayError(context.getString(R.string.incorrectSMS))
                    410 -> {
                        viewState.sayError(context.getString(R.string.allAttemptsWasBad))
                        cancelSmsPage()
                    }
                    else -> viewState . sayError (context.getString(R.string.serverError))
                }
            }
        }

        viewState.setButtonEnabled(true)
        viewState.setProgressBarEnabled(false)
    }

    fun sayDBError() {
        viewState.sayError(
            context.getString(R.string.dbError)
        )
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
        viewState.setButtonEnabled(true)
    }

    fun smsClicked(code: String) {
        interactor.smsCheck(userLogin!!, code.replace(" ", "").toInt(), attempts)
        attempts++
    }

    fun cancelSmsPage() {
        userLogin = null
        viewState.cancelTimeCoroutine()
        setRegistration()
    }

    fun requestNewSms() {
        attempts = 0
        viewState.setProgressBarEnabled(true)
        viewState.setButtonEnabled(false)

        interactor.getNewSms(userLogin!!.phone, false)
    }

    fun newSmsGot() {
        viewState.setProgressBarEnabled(false)
        viewState.setButtonEnabled(true)
        viewState.startTimeCoroutine()
    }
}