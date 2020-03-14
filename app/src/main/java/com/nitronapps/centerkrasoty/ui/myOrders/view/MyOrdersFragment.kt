package com.nitronapps.centerkrasoty.ui.myOrders.view

import android.widget.Toast
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.myOrders.presenter.MyOrdersPresenter
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MyOrdersView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)
}

class MyOrdersFragment: MvpAppCompatFragment(R.layout.fragment_my_orders),
        MyOrdersView {

    private val presenter by moxyPresenter { MyOrdersPresenter(context!!) }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}