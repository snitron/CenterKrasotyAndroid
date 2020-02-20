package com.nitronapps.centerkrasoty.ui.chooseService.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseService.interactor.ChooseServiceInteractor
import com.nitronapps.centerkrasoty.ui.chooseService.interactor.ChooseServiceInteractorInterface
import com.nitronapps.centerkrasoty.ui.chooseService.view.ChooseServiceView
import moxy.MvpPresenter
import java.util.*
import kotlin.concurrent.thread

class ChooseServicePresenter(val context: Context): MvpPresenter<ChooseServiceView>() {
    private lateinit var interactor: ChooseServiceInteractorInterface
    private lateinit var servicesRaw: Array<Service>

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ChooseServiceInteractor(this)

        interactor.prepareUserAndOffice()
    }

    fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun getServicesFromServer(){
        interactor.getServices()
    }

    fun servicesGot(services: Array<Service>){
        thread {
            servicesRaw = services

            val groups = mapOf<Int, ArrayList<Service>>().withDefault { arrayListOf() }

            services.forEach { groups.getValue(it.groupId).add(it) }

            viewState.setRecyclerView(groups)
        }
    }

    fun sayError(){
        viewState.sayError(context.getString(R.string.serverError))
    }
}
