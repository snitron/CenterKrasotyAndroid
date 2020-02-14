package com.nitronapps.centerkrasoty.ui.login.interactor
import android.util.Log
import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OrderDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.login.presenter.LoginPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

interface LoginInteractorInterface {
    fun login(login: String, password: String)
    fun register(login: String, password: String, phone: String)
}

class LoginInteractor(val presenter: LoginPresenter): LoginInteractorInterface {
    val userDatabase: UserDatabase
    val orderDatabase: OrderDatabase
    val api: IAPI

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).build()
        orderDatabase = Room.databaseBuilder(
            presenter.context,
            OrderDatabase::class.java,
            "orders_db"
        ).build()

        api = API.getRetrofitAPI()
    }

    override fun login(login: String, password: String) {
            api.loginByPassword(login, password, "")
                .subscribe({
                    if(it.code == 200) {
                        val user = UserInfo(0, it.token)
                        userDatabase.userDao().add(user)
                        presenter.startMain()
                    }

                    Log.w("USER_DB", "CODE: " + it.code)


                }, {
                    presenter.sayError()
                }).dispose()
    }

    override fun register(login: String, password: String, phone: String) {
        api.registerNewUser(login, password, phone)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    if(it.code == 200){
                        val userData = UserInfo(id = 0, token = it.token)
                        userDatabase.userDao().add(userData)
                    }
                },
                onError = {
                    this.presenter.sayError()
                },
                onComplete = {
                    this.presenter.startMain()
                }
            ).dispose()
    }

    private fun userCount(): Int {
        return userDatabase.userDao().getRowCount()
    }
}