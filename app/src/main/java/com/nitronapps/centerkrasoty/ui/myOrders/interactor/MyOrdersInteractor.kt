package com.nitronapps.centerkrasoty.ui.myOrders.interactor

import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.model.Order
import com.nitronapps.centerkrasoty.ui.myOrders.presenter.MyOrdersPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface MyOrdersInteractorInterface {
    fun disposeRequests()
    fun prepareUserAndGetOrders()
    fun getOrders()
    fun deleteOrder(orderId: Int)
}

class MyOrdersInteractor(val presenter: MyOrdersPresenter) :
    MyOrdersInteractorInterface {

    private val compositeDisposable = CompositeDisposable()

    private val userDatabase: UserDatabase
    private val api: IAPI

    private lateinit var userInfo: UserInfo

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).allowMainThreadQueries().build()

        api = API.getRetrofitAPI()
    }

    override fun getOrders() {
        compositeDisposable.add(
            api.getOrders(
                    userInfo.token,
                    forUser = true
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.code == 200 }
                .map { it.orders }
                .subscribe({},
                    { presenter.sayError() })
        )
    }

    override fun prepareUserAndGetOrders() {
        compositeDisposable.add(
            userDatabase.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        userInfo = it
                        getOrders()
                    },
                    { presenter.sayDBError() })
        )
    }

    override fun deleteOrder(orderId: Int) {
        compositeDisposable.add(
            api.deleteOrder(
                userInfo.token,
                orderId
            )   .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.code == 200 }
                .subscribe(
                    {
                        presenter.orderDeletedSuccessfully()
                    },
                    {
                        presenter.sayError()
                    }
                )
        )
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }
}