package com.nitronapps.centerkrasoty.ui.myAccount.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.myAccount.interactor.MyAccountInteractor
import com.nitronapps.centerkrasoty.ui.myAccount.interactor.MyAccountInteractorInterface
import com.nitronapps.centerkrasoty.ui.myAccount.view.MyAccountView
import moxy.MvpPresenter

class MyAccountPresenter(val context: Context): MvpPresenter<MyAccountView>() {
    private lateinit var interactor: MyAccountInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = MyAccountInteractor(this)
    }

    fun sayError(){
        viewState.sayError(
            context.getString(R.string.serverError)
        )
    }

}