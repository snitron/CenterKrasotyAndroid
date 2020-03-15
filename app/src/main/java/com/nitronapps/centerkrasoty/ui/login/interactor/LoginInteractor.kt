package com.nitronapps.centerkrasoty.ui.login.interactor

import android.util.Log
import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.login.presenter.LoginPresenter
import com.nitronapps.centerkrasoty.ui.login.view.LoginStatus
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.security.AlgorithmConstraints

interface LoginInteractorInterface {
    fun login(login: String, password: String)
    fun register(login: String, password: String, name: String, surname: String, phone: String)
    fun sms(code: Int)
    fun disposeRequests()
}

class LoginInteractor(val presenter: LoginPresenter) : LoginInteractorInterface {
    private val userDatabase: UserDatabase
    private val api: IAPI
    private val compositeDisposable = CompositeDisposable()
    private var loginInMemory = ""

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).build()

        api = API.getRetrofitAPI()
    }

    override fun login(login: String, password: String) {
        compositeDisposable.add(
            api.loginByPassword(login, password, "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.code == 200) {
                        val user = UserInfo(0, it.token)
                        compositeDisposable.add(
                            userDatabase.userDao().insert(user)
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                        )
                        presenter.startMain()
                    } else
                        presenter.sayError(LoginStatus.LOGIN, it.code)
                }, {
                    presenter.sayError(LoginStatus.LOGIN, 0)
                })
        )
    }

    override fun register(
        login: String,
        password: String,
        name: String,
        surname: String,
        phone: String
    ) {
        compositeDisposable.add(
            api.registerNewUser(login, password, name, surname, phone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onNext = {
                        if (it.code == 100) {
                            loginInMemory = it.login

                            presenter.startSMSPage()
                        } else
                            presenter.sayError(LoginStatus.REGISTRATION, it.code)
                    },
                    onError = {
                        this.presenter.sayError(LoginStatus.REGISTRATION, 0)
                    },
                    onComplete = {}
                )
        )
    }

    override fun sms(code: Int) {
        //TODO: 3 attempts for input (with server)
        //TODO: Check valid data (for server too)
        compositeDisposable.add(
            api.smsRequest(loginInMemory, code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.result) {
                            val user = UserInfo(0, it.token)
                            compositeDisposable.add(
                                userDatabase.userDao().insert(user)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe()
                            )
                            presenter.startMain()
                        } else {
                            presenter.sayError(LoginStatus.SMS_VERIFICATION, 204)
                        }
                    },
                    {
                        presenter.sayError(LoginStatus.SMS_VERIFICATION, 0)
                        Log.w("SMS_ERROR", it.localizedMessage)
                    }
                )
        )
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }

    private fun userCount(): Int {
        return userDatabase.userDao().getRowCount()
    }
}