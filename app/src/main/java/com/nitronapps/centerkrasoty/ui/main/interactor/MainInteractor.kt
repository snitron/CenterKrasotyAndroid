package com.nitronapps.centerkrasoty.ui.main.interactor

import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
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
    fun deleteAllData()
}

class MainInteractor(presenter: MainPresenter) : MainInteractorInterface {
    private val userDatabase: UserDatabase
    private val officeDatabase: OfficeDatabase
    private val api: IAPI
    private lateinit var userInfo: UserInfo

    private val compositeDisposable = CompositeDisposable()

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration().build()

        officeDatabase = Room.databaseBuilder(
            presenter.context,
            OfficeDatabase::class.java,
            "offices_db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigrationOnDowngrade().build()

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
            userDatabase.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .subscribe {
                    userInfo = it
                }
        )
    }

    override fun checkOffice(): Boolean {
        return officeDatabase.officeDao().getRowCount() == 0
    }

    override fun deleteAllData() {
        compositeDisposable.addAll(
            userDatabase.userDao().deleteAll()
                .subscribeOn(Schedulers.io())
                .subscribe(), //TODO: Maybe sayDBError?

            officeDatabase.officeDao().deleteAll()
                .subscribeOn(Schedulers.io())
                .subscribe()
        )
    }

}