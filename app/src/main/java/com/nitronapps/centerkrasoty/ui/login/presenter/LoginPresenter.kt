package com.nitronapps.centerkrasoty.ui.login.presenter

import android.content.Context
import android.util.Log
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.login.interactor.LoginInteractor
import com.nitronapps.centerkrasoty.ui.login.interactor.LoginInteractorInterface
import com.nitronapps.centerkrasoty.ui.login.view.LoginStatus
import com.nitronapps.centerkrasoty.ui.login.view.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter

class LoginPresenter(val context: Context): MvpPresenter<LoginView>() {
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

    fun loginClicked(login: String, password: String) {
        val api = API.getRetrofitAPI()

        api.loginByPassword(login, password, "")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.w("USER_DB", "CODE: " + it.code)
            }, {
                Log.w("USER_DB", "CODE: " + it.localizedMessage)
            }).dispose()
        interactor.login(login, password)
    }

    fun getCondition(): LoginStatus {
        return condition
    }

    fun sayError() {
        viewState.sayError()
        Log.w("ERROR_LOGIN", "LOGIN ERROR")
    }

    fun startMain() {
        viewState.startMainActivity()
    }
}