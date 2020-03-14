package com.nitronapps.centerkrasoty.ui.myAccount.view

import android.widget.Toast
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.myAccount.presenter.MyAccountPresenter
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MyAccountView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)
}

class MyAccountFragment: MvpAppCompatFragment(R.layout.fragment_choose_time),
        MyAccountView {

    private val presenter by moxyPresenter { MyAccountPresenter(context!!) }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}