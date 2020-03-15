package com.nitronapps.centerkrasoty.ui.confirm.interactor

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.Transaction
import com.google.gson.Gson
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.model.TransactionRequest
import com.nitronapps.centerkrasoty.ui.confirm.presenter.ConfirmPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface ConfirmInteractorInterface {
    fun disposeRequests()
    fun prepareUserAndSendTransactions(transactionRequests: List<TransactionRequest>)
    fun sendTransactions(transactionRequests: List<TransactionRequest>)
    fun getNotificationState(): Boolean
}

class ConfirmInteractor(val presenter: ConfirmPresenter):
    ConfirmInteractorInterface {

    private val compositeDisposable = CompositeDisposable()

    private val userDatabase: UserDatabase
    private val api: IAPI
    private val value: SharedPreferences

    private lateinit var userInfo: UserInfo

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).allowMainThreadQueries().build()

        api = API.getRetrofitAPI()

        value = presenter.context.getSharedPreferences("values", Context.MODE_PRIVATE)
    }

    override fun sendTransactions(transactionRequests: List<TransactionRequest>) {
        compositeDisposable.add(
            api.addTransaction(
                userInfo.token,
                Gson().toJson(transactionRequests)
            )   .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter{ it.code == 200 }
                .subscribe(
                    {
                        presenter.closeConfirm()
                    },
                    {
                        presenter.sayError()
                    }
                )
        )
    }

    override fun prepareUserAndSendTransactions(transactionRequests: List<TransactionRequest>) {
        compositeDisposable.add(
            userDatabase.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        userInfo = it
                        sendTransactions(transactionRequests)
                    },
                    {
                        presenter.sayDBError()
                    }
                )
        )
    }

    override fun getNotificationState(): Boolean {
        return value.getBoolean("notificationState", true)
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }
}