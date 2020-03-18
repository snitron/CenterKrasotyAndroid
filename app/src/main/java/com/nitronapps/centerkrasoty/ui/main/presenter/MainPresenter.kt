package com.nitronapps.centerkrasoty.ui.main.presenter

import android.content.Context
import androidx.core.app.NotificationCompat
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Place
import com.nitronapps.centerkrasoty.model.PreOrder
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.main.interactor.MainInteractor
import com.nitronapps.centerkrasoty.ui.main.interactor.MainInteractorInterface
import com.nitronapps.centerkrasoty.ui.view.MainView
import moxy.MvpPresenter
import kotlin.jvm.internal.Intrinsics


class MainPresenter(val context: Context) : MvpPresenter<MainView>() {
    private lateinit var interactor: MainInteractorInterface
    private var condition = TransactionStatus.OFFICE
    private var currentType = FragmentType.CREATE_ORDER
    private var isOrderCreating = true

    private var services = arrayListOf<Service>()
    private var currentServiceId = -1
    private var currentTime: Long = -1
    private val preOrders = arrayListOf<PreOrder>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = MainInteractor(this)

        if (interactor.checkLogin())
            viewState.startLoginPage()
        else
            interactor.prepareUser()

        if (interactor.checkOffice())
            condition = TransactionStatus.OFFICE
        else
            condition = TransactionStatus.SERVICE

        startByCondition(condition)

    }

    fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun startByCondition(status: TransactionStatus) {
        viewState.setFragmentByStatus(status)
    }

    fun invokedByBottomNavigationView(type: FragmentType, id: Int): Boolean {
        var check = true

        isOrderCreating = type === FragmentType.CREATE_ORDER

        if (currentType === type || condition === TransactionStatus.OFFICE) {
            check = false
        }
        val precompiledBoolean = check

        if (!(this.currentType === type || condition === TransactionStatus.OFFICE)) {
            this.currentType = type
            if (type === FragmentType.CREATE_ORDER) {
                calledCloseByConfirm()
            } else {
                viewState.setFragmentByType(type)
            }
            viewState.setItemOnBottomNavigationView(id)
        }

        return precompiledBoolean
    }

    fun calledCloseFromServices(services: ArrayList<Service>) {
        condition = TransactionStatus.TIME
        this.services = services
        this.currentServiceId = 0
        viewState.setFragmentByStatus(
            TransactionStatus.TIME,
            services[this.currentServiceId]
        )
    }

    fun calledInvokeChoosePlaceForTime(service: Service, time: Long) {
        condition = TransactionStatus.PLACE
        this.currentTime = time
        viewState.setFragmentByStatus(TransactionStatus.PLACE, service)
    }

    fun calledCloseFromChoosePlace(place: Place, service: Service) {
        this.preOrders.add(PreOrder(place, service, currentTime))

        currentTime = -1
        currentServiceId++

        if (currentServiceId != services.size) {
            condition = TransactionStatus.TIME

            viewState.setFragmentByStatus(
                status = TransactionStatus.TIME,
                service = services[currentServiceId]
            )
        } else {
            condition = TransactionStatus.CONFIRM

            viewState.setFragmentByStatus(
                status = TransactionStatus.CONFIRM,
                preOrders = this.preOrders
            )
        }
    }

    fun calledBackFromChoosePlace() {
        condition = TransactionStatus.TIME
        currentTime = -1

        viewState.setFragmentByStatus(
            condition,
            services[currentServiceId]
        )
    }

    fun calledBackFromChooseTime() {
        condition = TransactionStatus.SERVICE
        services.clear()

        viewState.setFragmentByStatus(
            condition
        )
    }

    fun calledCloseFromOffice() {
        condition = TransactionStatus.SERVICE

        viewState.setItemOnBottomNavigationView(R.id.itemCreateNewOrder)

        startByCondition(condition)
    }

    fun onBackPressed() {
        when (condition) {
            TransactionStatus.TIME ->
                calledBackFromChooseTime()

            TransactionStatus.PLACE ->
                calledBackFromChoosePlace()

            TransactionStatus.CONFIRM ->
                calledCloseByConfirm()

            else ->
                viewState.callSuperOnBackPressed()
        }
    }

    fun calledCloseByConfirm() {
        condition = TransactionStatus.SERVICE
        currentTime = -1
        currentServiceId = -1
        preOrders.clear()

        viewState.setFragmentByStatus(TransactionStatus.SERVICE)
    }

    fun calledCloseByMyAccount() {
        condition = TransactionStatus.OFFICE
        currentType = FragmentType.CREATE_ORDER
        currentTime = -1
        currentServiceId = -1
        preOrders.clear()

        viewState.setFragmentByStatus(TransactionStatus.OFFICE)
        viewState.setItemOnBottomNavigationView(R.id.itemCreateNewOrder)
    }

    fun logout() {
        interactor.deleteAllData()

        condition = TransactionStatus.OFFICE
        currentType = FragmentType.CREATE_ORDER

        currentTime = -1
        currentServiceId = -1
        preOrders.clear()

        viewState.startLoginPage()
    }
}

enum class TransactionStatus {
    OFFICE, SERVICE, PLACE, TIME, CONFIRM;
}

enum class FragmentType {
    CREATE_ORDER, MY_ORDERS, MY_ACCOUNT, ABOUT;
}