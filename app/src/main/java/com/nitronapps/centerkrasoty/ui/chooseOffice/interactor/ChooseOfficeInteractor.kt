package com.nitronapps.centerkrasoty.ui.chooseOffice.interactor

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.chooseOffice.presenter.ChooseOfficePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface ChooseOfficeInteractorInterface {
    fun getOffices()
    fun prepareUser()
    fun disposeRequests()
    fun deleteAllOfficesFromDB()
    fun addOffice(office: Office)
}

class ChooseOfficeInteractor(val presenter: ChooseOfficePresenter) :
    ChooseOfficeInteractorInterface {
    private val officeDatabase: OfficeDatabase
    private val userDatabase: UserDatabase
    private val api: IAPI
    private val constants: SharedPreferences

    private val compositeDisposable = CompositeDisposable()

    private lateinit var userInfo: UserInfo

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

        constants = presenter.context.getSharedPreferences("constants", Context.MODE_PRIVATE)
    }

    override fun getOffices() {
        compositeDisposable.add(
            api.getOffices(userInfo.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.code == 200 }
                .map { it.offices }
                .map { it.map { it.convertToOfficeDB() } }
                .subscribe(
                    { presenter.setOfficesToUI(it.toTypedArray()) },
                    { presenter.sayError() }
                )
        )
    }

    override fun prepareUser() {
        compositeDisposable.add(
            userDatabase.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    userInfo = it
                    presenter.loadOffices()
                },
                    {
                        presenter.sayDBError()
                    })
        )
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }

    override fun deleteAllOfficesFromDB() {
        compositeDisposable.add(
            officeDatabase.officeDao().deleteAll()
                .subscribeOn(Schedulers.io())
                .subscribe({}, {
                    presenter.sayDBError()
                })
        )
    }

    override fun addOffice(office: Office) {
        compositeDisposable.add(
            officeDatabase.officeDao().insert(office)
                .subscribeOn(Schedulers.io())
                .subscribe({}, {
                    presenter.sayDBError()
                })
        )
    }


}