package com.nitronapps.centerkrasoty.ui.chooseTime.view

import android.widget.Toast
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.chooseTime.presenter.ChooseTimePresenter
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ChooseTimeView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)
}

class ChooseTimeFragment: MvpAppCompatFragment(R.layout.fragment_choose_time),
        ChooseTimeView {

    private val presenter by moxyPresenter { ChooseTimePresenter(context!!) }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}