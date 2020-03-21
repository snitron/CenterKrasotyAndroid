package com.nitronapps.centerkrasoty.ui.myOrders.presenter

import android.content.Context
import androidx.room.FtsOptions
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Order
import com.nitronapps.centerkrasoty.ui.myOrders.interactor.MyOrdersInteractor
import com.nitronapps.centerkrasoty.ui.myOrders.interactor.MyOrdersInteractorInterface
import com.nitronapps.centerkrasoty.ui.myOrders.view.MyOrdersView
import moxy.MvpPresenter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MyOrdersPresenter(val context: Context): MvpPresenter<MyOrdersView>() {
    private lateinit var interactor: MyOrdersInteractorInterface
    private val orders = ArrayList<Order>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = MyOrdersInteractor(this)
        interactor.prepareUserAndGetOrders()

        viewState.setSwipeRefreshViewLayoutRefreshing(true)
    }

    fun sayError(){
        viewState.sayError(
            context.getString(R.string.serverError)
        )
        viewState.setSwipeRefreshViewLayoutRefreshing(false)
        calculateOrdersAndSet()
    }

    fun sayDBError() {
        viewState.sayError(
            context.getString(R.string.dbError)
        )
        viewState.setSwipeRefreshViewLayoutRefreshing(false)
        calculateOrdersAndSet()
    }

    fun onDestroyCalled(){
        interactor.disposeRequests()
    }

    fun getOrders(){
        interactor.getOrders()

        viewState.setSwipeRefreshViewLayoutRefreshing(true)
        //TODO: Maybe lazy loading for items
    }

    fun ordersGot(orders: ArrayList<Order>) {
        this.orders.clear()
        this.orders.addAll(orders)

        viewState.setSwipeRefreshViewLayoutRefreshing(false)
        calculateOrdersAndSet()
    }

    fun userChosenToDeleteOrder(order: Order) {
        orders.remove(order)

        viewState.clearRecyclerView()
        viewState.setSwipeRefreshViewLayoutRefreshing(true)

        interactor.deleteOrder(order.id)
    }

    fun orderDeletedSuccessfully(){
        viewState.setSwipeRefreshViewLayoutRefreshing(false)
        calculateOrdersAndSet()
    }

    fun calculateOrdersAndSet(){
        viewState.setSwipeRefreshViewLayoutRefreshing(true)
        viewState.clearRecyclerView()

        thread {
            val currentDate = Calendar.getInstance().time
            val data = mutableMapOf<String, Pair<ArrayList<Order>, Boolean>>()

            orders.sort()

            if (orders.size != 0){
                if (orders.all { it.checkIsFutureOrder(currentDate) }) {
                    data[context.getString(R.string.newOrders)] = Pair(orders, true)
                } else {
                    val newOrders = arrayListOf<Order>()
                    val pastOrders = arrayListOf<Order>()

                    orders.forEach {
                        if (it.checkIsFutureOrder(currentDate))
                            newOrders.add(it)
                        else
                            pastOrders.add(it)
                    }

                    if(newOrders.size != 0)
                        data[context.getString(R.string.newOrders)] = Pair(newOrders, true)

                    if(pastOrders.size != 0)
                        data[context.getString(R.string.pastOrders)] = Pair(pastOrders, false)
                }
            }

            viewState.setRecyclerView(data)
        }
    }

}