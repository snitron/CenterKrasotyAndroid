package com.nitronapps.centerkrasoty.ui.myAccount.interactor

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.SharedSQLiteStatement
import com.nitronapps.centerkrasoty.api.API
import com.nitronapps.centerkrasoty.api.IAPI
import com.nitronapps.centerkrasoty.data.OfficeDatabase
import com.nitronapps.centerkrasoty.data.UserDatabase
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.data.entity.UserInfo
import com.nitronapps.centerkrasoty.ui.myAccount.presenter.MyAccountPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.concurrent.thread

interface MyAccountInteractorInterface {
    fun disposeRequests()
    fun prepareUserAndOfficeAndGetUserInfo()
    fun getData()
    fun getNotificationState(): Boolean
    fun setNotificationState(state: Boolean)
}

class MyAccountInteractor(val presenter: MyAccountPresenter):
    MyAccountInteractorInterface {

    private val compositeDisposable = CompositeDisposable()

    private val userDatabase: UserDatabase
    private val officeDatabase: OfficeDatabase
    private val api: IAPI
    private val values: SharedPreferences

    private lateinit var userInfo: UserInfo

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

    override fun prepareUserAndOfficeAndGetUserInfo() {
        compositeDisposable.add(
            userDatabase.userDao().getAll()
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    userInfo = it

                    getData()
                },
                    {
                    presenter.sayDBError()
                })
        )
    }

    override fun getData() {
        compositeDisposable.addAll(
            api.getUser(userInfo.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter { it.code == 200 }
                .subscribe(
                    {
                        presenter.userInfoGot(it.name, it.surname)
                    },
                    {
                        presenter.sayError()
                    }
                ),

            officeDatabase.officeDao().getAll()
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        presenter.officeGot(it)
                    },
                    {
                        presenter.sayDBError()
                    }
                )
        )
    }

    override fun getNotificationState(): Boolean {
        return values.getBoolean("notificationState", true)
    }

    override fun setNotificationState(state: Boolean) {
        thread {
            values.edit().putBoolean("notificationState", state).apply()
        }
    }

    override fun disposeRequests() {
        compositeDisposable.clear()
    }
}