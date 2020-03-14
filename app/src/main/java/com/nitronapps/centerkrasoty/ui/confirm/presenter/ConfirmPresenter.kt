package com.nitronapps.centerkrasoty.ui.confirm.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.confirm.interactor.ConfirmInteractor
import com.nitronapps.centerkrasoty.ui.confirm.interactor.ConfirmInteractorInterface
import com.nitronapps.centerkrasoty.ui.confirm.view.ConfirmView
import moxy.MvpPresenter

class ConfirmPresenter(val context: Context): MvpPresenter<ConfirmView>() {
    private lateinit var interactor: ConfirmInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ConfirmInteractor(this)
    }

    fun sayError(){
        viewState.sayError(
            context.getString(R.string.serverError)
        )
    }

}