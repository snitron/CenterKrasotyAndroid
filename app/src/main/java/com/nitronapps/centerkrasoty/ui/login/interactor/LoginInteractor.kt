package com.nitronapps.centerkrasoty.ui.login.interactor

import android.util.Log
import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.model.UserLogin
import com.nitronapps.centerkrasoty.ui.login.presenter.LoginPresenter
import com.nitronapps.centerkrasoty.ui.login.view.LoginStatus
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.security.AlgorithmConstraints

interface LoginInteractorInterface {
    fun login(phone: String, password: String)
    fun register(phone: String, password: String, name: String, surname: String)
    fun smsCheck(userLogin: UserLogin, code: Int, attempts: Int)
    fun getNewSms(phone: String, first: Boolean)
    fun disposeRequests()
}

class LoginInteractor(val presenter: LoginPresenter) : LoginInteractorInterface {
    private val userDatabase: UserDatabase
    private val api: IAPI
    private val compositeDisposable = CompositeDisposable()

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).fallbackToDestructiveMigration().build()

        api = API.getRetrofitAPI()
    }

    override fun login(phone: String, password: String) {
        compositeDisposable.add(
            api.loginByPassword(phone, password)
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
        phone: String,
        password: String,
        name: String,
        surname: String
    ) {
        compositeDisposable.add(
            api.registerNewUser(phone, password, name, surname)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onNext = {
                        if (it.code == 100) {
                            val user = UserInfo(0, it.token)
                            compositeDisposable.add(
                                userDatabase.userDao().insert(user)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe({
                                        presenter.startMain()
                                    }, {
                                        presenter.sayDBError()
                                    })
                            )
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

    override fun smsCheck(userLogin: UserLogin, code: Int, attempts: Int) {
        //TODO: Check valid data (for server too)
        compositeDisposable.add(
            api.smsRequest(userLogin.phone, code, attempts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if (it.code == 200) {
                            register(
                                phone = userLogin.phone,
                                password = userLogin.password,
                                name = userLogin.name,
                                surname = userLogin.surname
                            )
                        } else {
                            presenter.sayError(LoginStatus.SMS_VERIFICATION, it.code)
                        }
                    },
                    {
                        presenter.sayError(LoginStatus.SMS_VERIFICATION, 0)
                    }
                )
        )
    }

    override fun getNewSms(phone: String, first: Boolean) {
        compositeDisposable.add(
            api.sendNewSms(phone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.code == 200) {
                            if (first)
                                presenter.startSMSPage()
                            else
                                presenter.newSmsGot()
                        } else
                            presenter.sayError(LoginStatus.SMS_VERIFICATION, it.code)
                    },
                    {
                        presenter.sayError(LoginStatus.SMS_VERIFICATION, 500)
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