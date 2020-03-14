package com.nitronapps.centerkrasoty.ui.myOrders.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.myOrders.interactor.MyOrdersInteractor
import com.nitronapps.centerkrasoty.ui.myOrders.interactor.MyOrdersInteractorInterface
import com.nitronapps.centerkrasoty.ui.myOrders.view.MyOrdersView
import moxy.MvpPresenter

class MyOrdersPresenter(val context: Context): MvpPresenter<MyOrdersView>() {
    private lateinit var interactor: MyOrdersInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = MyOrdersInteractor(this)
    }

    fun sayError(){
        viewState.sayError(
            context.getString(R.string.serverError)
        )
    }

}