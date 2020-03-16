package com.nitronapps.centerkrasoty.ui.myAccount.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.ui.myAccount.interactor.MyAccountInteractor
import com.nitronapps.centerkrasoty.ui.myAccount.interactor.MyAccountInteractorInterface
import com.nitronapps.centerkrasoty.ui.myAccount.view.MyAccountView
import moxy.MvpPresenter

class MyAccountPresenter(val context: Context) : MvpPresenter<MyAccountView>() {
    private lateinit var interactor: MyAccountInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = MyAccountInteractor(this)

        interactor.prepareUserAndOfficeAndGetUserInfo()
        viewState.setSwitchState(interactor.getNotificationState())
    }

    fun sayError() {
        viewState.sayError(
            context.getString(R.string.serverError)
        )
    }

    fun sayDBError() {
        viewState.sayError(
            context.getString(R.string.dbError)
        )
    }

    fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun userInfoGot(name: String, surname: String) {
        viewState.setUser(name, surname)
    }

    fun officeGot(office: Office) {
        viewState.setOffice(office, prepareYandexStaticApi(office.geoCoords))
    }

    private fun prepareYandexStaticApi(coords: String): String {
        return "https://static-maps.yandex.ru/1.x/?ll=" +
                coords.split(" ").reversed().joinToString(",") +
                "&size=450,300&z=17&l=map"
    }

    fun switchChanged(newState: Boolean) {
        interactor.setNotificationState(newState)
    }
}