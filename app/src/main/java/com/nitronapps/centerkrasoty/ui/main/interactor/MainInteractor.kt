package com.nitronapps.centerkrasoty.ui.main.interactor

import androidx.room.Room
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OrderDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.ui.main.presenter.MainPresenter

interface MainInteractorInterface {
    fun checkLogin(): Boolean
}

class MainInteractor(presenter: MainPresenter) : MainInteractorInterface {
    val userDatabase: UserDatabase
    val orderDatabase: OrderDatabase
    val api: IAPI

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

        api = API.getRetrofitAPI()
    }

    override fun checkLogin(): Boolean {
        return userDatabase.userDao().getRowCount() == 0
    }

}