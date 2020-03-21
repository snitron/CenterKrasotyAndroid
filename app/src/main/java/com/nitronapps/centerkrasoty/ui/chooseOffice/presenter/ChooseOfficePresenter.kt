package com.nitronapps.centerkrasoty.ui.chooseOffice.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.ui.chooseOffice.interactor.ChooseOfficeInteractor
import com.nitronapps.centerkrasoty.ui.chooseOffice.interactor.ChooseOfficeInteractorInterface
import com.nitronapps.centerkrasoty.ui.chooseOffice.view.ChooseOfficeFragment
import com.nitronapps.centerkrasoty.ui.chooseOffice.view.ChooseOfficeView
import moxy.MvpPresenter

class ChooseOfficePresenter(val context: Context) : MvpPresenter<ChooseOfficeView>() {
    private lateinit var interactor: ChooseOfficeInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ChooseOfficeInteractor(this)
        interactor.prepareUser()
    }

    fun officeChosenByUser(office: Office) {
        interactor.deleteAllOfficesFromDBandAddOffice(office)

        viewState.makeProgressBar(true)
        viewState.setRecyclerViewEnabledBy(false)
    }

    fun allSaved() {
        viewState.makeProgressBar(false)

        viewState.closeFragment()
    }

    fun loadOffices() {
        interactor.getOffices()
    }

    fun setOfficesToUI(offices: ArrayList<Office>) {
        viewState.setRecyclerViewOffices(offices)
    }

    fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun sayError() {
        viewState.sayErrorToUser(context.getString(R.string.serverError))
        viewState.makeProgressBar(false)
        viewState.setRecyclerViewEnabledBy(true)
    }

    fun sayDBError() {
        viewState.sayErrorToUser(
            context.getString(R.string.dbError)
        )
        viewState.makeProgressBar(false)
        viewState.setRecyclerViewEnabledBy(true)


    }
}