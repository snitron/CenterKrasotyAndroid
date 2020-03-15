package com.nitronapps.centerkrasoty.ui.chooseService.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseService.interactor.ChooseServiceInteractor
import com.nitronapps.centerkrasoty.ui.chooseService.interactor.ChooseServiceInteractorInterface
import com.nitronapps.centerkrasoty.ui.chooseService.view.ChooseServiceView
import moxy.MvpPresenter
import java.lang.String
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class ChooseServicePresenter(val context: Context) : MvpPresenter<ChooseServiceView>() {
    private lateinit var interactor: ChooseServiceInteractorInterface
    private lateinit var servicesRaw: ArrayList<Service>
    private val chosenIds = arrayListOf<Int>()
    private val servicesCurrent = arrayListOf<Service>()
    private val chosenGroups = arrayListOf<Int>()
    private var value = 0.0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ChooseServiceInteractor(this)

        interactor.prepareUserAndOfficeAndGetServices()

        viewState.setButtonContinueEnabled(false)
        viewState.setRecyclerViewRefreshing(true)
    }

    fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun sayDBError() {
        viewState.sayError(
            context.getString(R.string.dbError)
        )
    }

    fun reloadServicesFromServer(){
        interactor.disposeRequests()
        interactor.getServices()

        servicesCurrent.clear()
        chosenIds.clear()
        chosenGroups.clear()

        setValueToViewState()
        viewState.setButtonContinueEnabled(false)
    }

    fun servicesGot(services: ArrayList<Service>) {
        thread {
            servicesRaw = services
            servicesCurrent.clear()
            servicesCurrent.addAll(services)

            calculateServicesMapAndSet(true)
        }
    }

    fun sayError() {
        viewState.sayError(context.getString(R.string.serverError))
    }

    private fun calculateServicesMapAndSet(first: Boolean) {
        if(first)
            viewState.setRecyclerViewRefreshing(true)

        val groups = mutableMapOf<Int, ArrayList<Service>>().withDefault { arrayListOf() }

        for (it in servicesCurrent) {
            if (!groups.containsKey(it.groupId)) {
                groups[it.groupId] = arrayListOf()
            }

            groups[it.groupId]!!.add(it)
        }
        if (first) {
            viewState.setFirstRecyclerView(groups)
        } else {
            viewState.setRecyclerViewAgain(
                groups,
                chosenGroups,
                chosenIds
            )
        }
    }

    fun checkedServiceByUser(service: Service, state: Boolean) {
        if (state) {
            this.chosenIds.add(Integer.valueOf(service.id))
            this.chosenGroups.add(Integer.valueOf(service.groupId))
            value += service.price
        } else {
            this.chosenIds.remove(Integer.valueOf(service.id))
            this.chosenGroups.remove(Integer.valueOf(service.groupId))
            value -= service.price
        }
        viewState.setButtonContinueEnabled(chosenIds.size != 0)
        setValueToViewState()
        calculateServicesMapAndSet(false)
    }

    private fun setValueToViewState() {
        val format =
            String.format(Locale("ru", "RU"), "%.2f", value)
        val sb = StringBuilder()
        sb.append(format)
        sb.append(" ₽")
        viewState.updateTotalValue(sb.toString())
    }

    fun confirmButtonClicked(){
        val servicesToSend = servicesRaw.filter { chosenIds.contains(it.id) }

        viewState.closeFragmentByRemote(ArrayList(servicesToSend))
    }
}
