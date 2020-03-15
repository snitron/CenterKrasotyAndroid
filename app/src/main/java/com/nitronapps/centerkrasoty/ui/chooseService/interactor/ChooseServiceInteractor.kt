package com.nitronapps.centerkrasoty.ui.chooseService.interactor

import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.chooseService.presenter.ChooseServicePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*

interface ChooseServiceInteractorInterface {
    fun prepareUserAndOfficeAndGetServices()
    fun disposeRequests()
    fun getServices()
}

class ChooseServiceInteractor(val presenter: ChooseServicePresenter) :
    ChooseServiceInteractorInterface {
    private val officeDatabase: OfficeDatabase
    private val userDatabase: UserDatabase
    private val api: IAPI

    private lateinit var userInfo: UserInfo
    private lateinit var office: Office

    private val compositeDisposable = CompositeDisposable()

    init {
        officeDatabase = Room.databaseBuilder(
            presenter.context,
            OfficeDatabase::class.java,
            "offices_db"
        ).build()

        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).build()

        api = API.getRetrofitAPI()
    }

    override fun prepareUserAndOfficeAndGetServices() {
        compositeDisposable.add(
            Observable.zip(
                    userDatabase.userDao().getAll(),
                    officeDatabase.officeDao().getAll(),
                    BiFunction<UserInfo, Office, Unit>{ userInfo, office ->
                        this.userInfo = userInfo
                        this.office = office
                        getServices()
                    }
            ).subscribeOn(Schedulers.io())
                .doOnError { presenter.sayDBError() }
                .subscribe()
        )
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }

    override fun getServices() {
        compositeDisposable.add(
            api.getServices(userInfo.token, office.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.code == 200 }
                .map { it.services }
                .subscribe(
                    { presenter.servicesGot(it) },
                    { presenter.sayError() }
                )
        )
    }


}