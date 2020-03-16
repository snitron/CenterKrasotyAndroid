package com.nitronapps.centerkrasoty.ui.chooseTime.interactor

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.chooseTime.presenter.ChooseTimePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.concurrent.thread

interface ChooseTimeInteractorInterface {
    fun disposeRequests()
    fun getOrders(groupServiceId: Int, date: String)
    fun prepareUserAndOfficeAndGetOrders(groupServiceId: Int, date: String)
    fun getOffice(): Office
    fun setUnavailablePlaces(places: ArrayList<Int>)
}

class ChooseTimeInteractor(val presenter: ChooseTimePresenter):
    ChooseTimeInteractorInterface {

    private val compositeDisposable = CompositeDisposable()

    private val userDatabase: UserDatabase
    private val officeDatabase: OfficeDatabase
    private val api: IAPI
    private val values: SharedPreferences

    private lateinit var userInfo: UserInfo
    private lateinit var office: Office

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        officeDatabase = Room.databaseBuilder(
            presenter.context,
            OfficeDatabase::class.java,
            "offices_db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        api = API.getRetrofitAPI()

        values = presenter.context.getSharedPreferences("values", Context.MODE_PRIVATE)
    }

    override fun getOrders(groupServiceId: Int, date: String) {
        compositeDisposable.add(
            api.getOrders(
                token = userInfo.token,
                date = date,
                groupServiceId = groupServiceId,
                forUser = false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter{ it.code == 200 }
                .map {
                    presenter.setCount(it.count)
                    it.orders
                }.subscribe(
                    { presenter.ordersGot(ArrayList(it)) },
                    { presenter.sayError() }
                )
        )
    }

    override fun prepareUserAndOfficeAndGetOrders(groupServiceId: Int, date: String) {
        compositeDisposable.addAll(
            userDatabase.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    userInfo = it
                    getOrders(groupServiceId, date)
                },
                    { presenter.sayDBError() }),

            officeDatabase.officeDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    office = it
                },
                    { presenter.sayDBError() })
        )
    }

    override fun setUnavailablePlaces(places: ArrayList<Int>) {
        thread {
            values.edit().putString("unavailablePlaces", Gson().toJson(places)).apply()
        }
    }

    override fun getOffice(): Office {
        return office
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }
}