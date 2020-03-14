package com.nitronapps.centerkrasoty.ui.chooseTime.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.chooseTime.interactor.ChooseTimeInteractor
import com.nitronapps.centerkrasoty.ui.chooseTime.interactor.ChooseTimeInteractorInterface
import com.nitronapps.centerkrasoty.ui.chooseTime.view.ChooseTimeView
import moxy.MvpPresenter

class ChooseTimePresenter(val context: Context): MvpPresenter<ChooseTimeView>() {
    private lateinit var interactor: ChooseTimeInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ChooseTimeInteractor(this)
    }

    fun sayError(){
        viewState.sayError(
            context.getString(R.string.serverError)
        )
    }

}