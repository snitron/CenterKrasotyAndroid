package com.nitronapps.centerkrasoty.ui.main.interactor

import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.OrderDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.main.presenter.MainPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface MainInteractorInterface {
    fun checkLogin(): Boolean
    fun checkOffice(): Boolean
    fun disposeRequests()
    fun prepareUser()
}

class MainInteractor(presenter: MainPresenter) : MainInteractorInterface {
    private val userDatabase: UserDatabase
    private val orderDatabase: OrderDatabase
    private val officeDatabase: OfficeDatabase
    private val api: IAPI
    private lateinit var userInfo: UserInfo

    private val compositeDisposable = CompositeDisposable()

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).allowMainThreadQueries().build()

        orderDatabase = Room.databaseBuilder(
            presenter.context,
            OrderDatabase::class.java,
            "orders_db"
        ).build()

        officeDatabase = Room.databaseBuilder(
            presenter.context,
            OfficeDatabase::class.java,
            "offices_db"
        ).allowMainThreadQueries().build()

        api = API.getRetrofitAPI()
    }

    override fun checkLogin(): Boolean {
        return userDatabase.userDao().getRowCount() == 0
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }

    override fun prepareUser() {
        compositeDisposable.add(
            userDatabase.userDao().getById(0)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    userInfo = it
                }
        )
    }

    override fun checkOffice(): Boolean {
        return officeDatabase.officeDao().getRowCount() == 0
    }

}