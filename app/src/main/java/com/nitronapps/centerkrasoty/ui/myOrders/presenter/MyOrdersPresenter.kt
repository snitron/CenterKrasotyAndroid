package com.nitronapps.centerkrasoty.ui.myOrders.presenter

import android.content.Context
import androidx.room.FtsOptions
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Order
import com.nitronapps.centerkrasoty.ui.myOrders.interactor.MyOrdersInteractor
import com.nitronapps.centerkrasoty.ui.myOrders.interactor.MyOrdersInteractorInterface
import com.nitronapps.centerkrasoty.ui.myOrders.view.MyOrdersView
import moxy.MvpPresenter

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
        viewState.setRecyclerView(orders)
    }

    fun sayDBError() {
        viewState.sayError(
            context.getString(R.string.dbError)
        )
        viewState.setSwipeRefreshViewLayoutRefreshing(false)
        viewState.setRecyclerView(orders)
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
        viewState.setRecyclerView(orders)
    }

    fun userChosenToDeleteOrder(order: Order) {
        orders.remove(order)

        viewState.clearRecyclerView()
        viewState.setSwipeRefreshViewLayoutRefreshing(true)

        interactor.deleteOrder(order.id)
    }

    fun orderDeletedSuccessfully(){
        viewState.setSwipeRefreshViewLayoutRefreshing(false)
        viewState.setRecyclerView(orders)
    }

}