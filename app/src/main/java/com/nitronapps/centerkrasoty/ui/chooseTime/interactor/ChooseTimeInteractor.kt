package com.nitronapps.centerkrasoty.ui.chooseTime.interactor

import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.chooseTime.presenter.ChooseTimePresenter
import io.reactivex.disposables.CompositeDisposable

interface ChooseTimeInteractorInterface {
    fun disposeRequests()
}

class ChooseTimeInteractor(val presenter: ChooseTimePresenter):
    ChooseTimeInteractorInterface {

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

    override fun disposeRequests() {
        compositeDisposable.clear()
    }
}