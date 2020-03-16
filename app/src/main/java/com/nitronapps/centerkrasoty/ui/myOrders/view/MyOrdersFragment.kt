package com.nitronapps.centerkrasoty.ui.myOrders.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Order
import com.nitronapps.centerkrasoty.ui.myOrders.adapter.MyOrdersAdapter
import com.nitronapps.centerkrasoty.ui.myOrders.presenter.MyOrdersPresenter
import kotlinx.android.synthetic.main.fragment_my_orders.*
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import okhttp3.internal.threadFactory

interface MyOrdersView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun clearRecyclerView()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerView(orders: ArrayList<Order>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSwipeRefreshViewLayoutRefreshing(by: Boolean)
}

interface MyOrdersRemote {
    fun userChosenToDelete(order: Order)
}

class MyOrdersFragment: MvpAppCompatFragment(R.layout.fragment_my_orders),
        MyOrdersView,
        MyOrdersRemote {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipeRefershLayoutMyOrders.setOnRefreshListener {
            presenter.getOrders()
        }

        recyclerViewMyOrders.layoutManager = LinearLayoutManager(context!!)
    }

    private val presenter by moxyPresenter { MyOrdersPresenter(context!!) }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        presenter.onDestroyCalled()
        super.onDestroyView()
    }

    override fun setSwipeRefreshViewLayoutRefreshing(by: Boolean) {
        activity!!.runOnUiThread {
            swipeRefershLayoutMyOrders.isRefreshing = by
        }
    }

    override fun clearRecyclerView() {
        activity!!.runOnUiThread {
            recyclerViewMyOrders.adapter = null
        }
    }

    override fun setRecyclerView(orders: ArrayList<Order>) {
        activity!!.runOnUiThread {
            recyclerViewMyOrders.adapter = MyOrdersAdapter(orders, this)
        }
    }

    override fun userChosenToDelete(order: Order) {
        activity!!.runOnUiThread {
            AlertDialog.Builder(context!!)
                .setTitle(R.string.areYouSure)
                .setMessage(getString(R.string.areYouSureToDelete) + " ${order.serviceName}?")
                .setPositiveButton(R.string.delete) { dialogInterface, _ ->  presenter.userChosenToDeleteOrder(order); dialogInterface.dismiss()}
                .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
                .show()
        }
    }
}