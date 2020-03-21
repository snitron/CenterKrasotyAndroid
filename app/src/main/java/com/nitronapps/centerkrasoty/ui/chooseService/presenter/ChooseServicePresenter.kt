package com.nitronapps.centerkrasoty.ui.chooseService.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseService.interactor.ChooseServiceInteractor
import com.nitronapps.centerkrasoty.ui.chooseService.interactor.ChooseServiceInteractorInterface
import com.nitronapps.centerkrasoty.ui.chooseService.view.ChooseServiceView
import moxy.MvpPresenter
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class ChooseServicePresenter(val context: Context) : MvpPresenter<ChooseServiceView>() {
    private lateinit var interactor: ChooseServiceInteractorInterface
    private lateinit var servicesRaw: ArrayList<Service>
    private val chosenIds = arrayListOf<Int>()
    private val servicesCurrent = arrayListOf<Service>()
    private val chosenGroups = arrayListOf<Int>()
    private val cellsWithServiceIds = mutableMapOf<Long, Pair<Int, Int>>()
    // Pair - serviceId, groupServiceId


    private var value = 0.0


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ChooseServiceInteractor(this)

        interactor.prepareUserAndOfficeAndGetServices()

        viewState.setButtonContinueEnabled(false)
        viewState.setRecyclerViewRefreshing(true)
        setValueToViewState()
    }

    fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun sayDBError() {
        viewState.sayError(
            context.getString(R.string.dbError)
        )
    }

    fun reloadServicesFromServer() {
        interactor.disposeRequests()
        interactor.getServices()

        servicesCurrent.clear()
        chosenIds.clear()
        chosenGroups.clear()
        cellsWithServiceIds.clear()

        value = 0.0

        setValueToViewState()
        viewState.setButtonContinueEnabled(false)
    }

    fun servicesGot(services: ArrayList<Service>) {
        thread {
            services.sort()
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
        if (first)
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
            /*  viewState.setRecyclerViewAgain(
                groups,
                chosenGroups,
                chosenIds
            )*/
        }
    }

    fun checkedServiceByUser(service: Service, state: Boolean) {
        if (state) {
            this.chosenIds.add(service.id)
            this.chosenGroups.add(service.groupId)
            value += service.price
        } else {
            this.chosenIds.remove(service.id)
            this.chosenGroups.remove(service.groupId)
            value -= service.price
        }
        viewState.setButtonContinueEnabled(chosenIds.size != 0)
        setValueToViewState()
        //calculateServicesMapAndSet(false)
    }

    private fun setValueToViewState() {
        val format =
            String.format(Locale("ru", "RU"), "%.2f", value)
        val sb = StringBuilder()
        sb.append(format)
        sb.append(" ₽")
        viewState.updateTotalValue(sb.toString())
    }

    fun confirmButtonClicked() {
        val servicesToSend = servicesRaw.filter { chosenIds.contains(it.id) }

        viewState.closeFragmentByRemote(ArrayList(servicesToSend))
    }

    fun getPossibilityOfEditingItem(id: Long): Boolean {
        return try {
            (!chosenGroups.contains(cellsWithServiceIds[id]!!.second)
                    || chosenIds.contains(cellsWithServiceIds[id]!!.first))
        } catch (e: Exception) {
            false
        }
    }

    fun registerCell(id: Long, serviceId: Int, groupServiceId: Int) {
        cellsWithServiceIds[id] = Pair(serviceId, groupServiceId)
    }

    fun getChecked(id: Long): Boolean {
        return chosenIds.contains(
            cellsWithServiceIds[id]!!.first
        )
    }
}

