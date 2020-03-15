package com.nitronapps.centerkrasoty.ui.choosePlace.interactor

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.choosePlace.presenter.ChoosePlacePresenter
import com.nitronapps.centerkrasoty.utils.fromJson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import kotlin.concurrent.thread

interface ChoosePlaceInteractorInterface {
    fun disposeRequests()
    fun getPlaces(groupServiceId: Int)
    fun prapareUserAndGetPlaces(groupServiceId: Int)
    fun getListOfUnavailablePlaces(): ArrayList<Int>
}

class ChoosePlaceInteractor(val presenter: ChoosePlacePresenter) :
    ChoosePlaceInteractorInterface {

    private val compositeDisposable = CompositeDisposable()

    private val userDatabase: UserDatabase
    private val api: IAPI
    private val values: SharedPreferences

    private lateinit var userInfo: UserInfo

    init {
        userDatabase = Room.databaseBuilder(
            presenter.context,
            UserDatabase::class.java,
            "users_db"
        ).allowMainThreadQueries().build()

        api = API.getRetrofitAPI()

        values = presenter.context.getSharedPreferences("values", Context.MODE_PRIVATE)
    }

    override fun getPlaces(groupServiceId: Int) {
        compositeDisposable.add(
            api.getPlaces(
                    userInfo.token,
                    groupServiceId
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.code == 200 }
                .map { it.places }
                .subscribe(
                    { presenter.placesGot(ArrayList(it)) },
                    { presenter.sayError() }
                )
        )
    }

    override fun prapareUserAndGetPlaces(groupServiceId: Int) {
        compositeDisposable.add(
            userDatabase.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    userInfo = it
                    getPlaces(groupServiceId)
                }, {
                    presenter.sayDBError()
                })
        )
    }

    override fun getListOfUnavailablePlaces(): ArrayList<Int> {
        try {
            return Gson().fromJson<ArrayList<Int>>(
                values.getString("unavailablePlaces", "[]")!!
            )
        } catch (e: Exception) {
            presenter.sayDBError()
            return ArrayList()
        }
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }
}