package com.nitronapps.centerkrasoty.ui.confirm.view

import android.widget.Toast
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.confirm.presenter.ConfirmPresenter
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ConfirmView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)
}

class ConfirmFragment: MvpAppCompatFragment(R.layout.fragment_confirm),
        ConfirmView {

    private val presenter by moxyPresenter { ConfirmPresenter(context!!) }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}