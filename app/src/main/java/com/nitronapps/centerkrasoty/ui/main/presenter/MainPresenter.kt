package com.nitronapps.centerkrasoty.ui.main.presenter

import android.content.Context
import android.content.Intent
import com.nitronapps.centerkrasoty.ui.login.view.LoginActivity
import com.nitronapps.centerkrasoty.ui.main.interactor.MainInteractor
import com.nitronapps.centerkrasoty.ui.main.interactor.MainInteractorInterface
import com.nitronapps.centerkrasoty.ui.view.MainView
import moxy.MvpPresenter

class MainPresenter(val context: Context): MvpPresenter<MainView>() {
    lateinit var interactor: MainInteractorInterface
    private var condition = TransactionStatus.OFFICE

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = MainInteractor(this)

        if(interactor.checkLogin())
            viewState.startLoginPage()
        else
            interactor.prepareUser()

        if(interactor.checkOffice())
            condition = TransactionStatus.OFFICE
        else
            condition = TransactionStatus.SERVICE

        startByCondition(condition)

    }

    fun onDestroyCalled(){
        interactor.disposeRequests()
    }

    fun startByCondition(status: TransactionStatus) {
        viewState.setFragmentByStatus(status)
    }

}

enum class TransactionStatus {
    OFFICE, SERVICE, PLACE, TIME;
}