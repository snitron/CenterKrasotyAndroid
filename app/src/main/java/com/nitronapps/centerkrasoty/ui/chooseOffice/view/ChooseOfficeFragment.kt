package com.nitronapps.centerkrasoty.ui.chooseOffice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.ui.chooseOffice.adapter.ChooseOfficeItemAdapter
import com.nitronapps.centerkrasoty.ui.chooseOffice.presenter.ChooseOfficePresenter
import com.nitronapps.centerkrasoty.ui.main.presenter.TransactionStatus
import com.nitronapps.centerkrasoty.ui.view.MainFragmentRemote
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_choose_office.*
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import okhttp3.internal.notifyAll

interface ChooseOfficeView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun makeProgressBar(animate: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerViewOffices(offices: ArrayList<Office>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayErrorToUser(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeFragment()
}

interface ChooseOfficeItemRemoteInterface {
    fun officeChosen(office: Office)
}

class ChooseOfficeFragment(private val remote: MainFragmentRemote) :
    MvpAppCompatFragment(R.layout.fragment_choose_office),
    ChooseOfficeView,
    ChooseOfficeItemRemoteInterface {

    private val presenter by moxyPresenter { ChooseOfficePresenter(context!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_office, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipeRefreshLayoutOffice.setOnRefreshListener {
            presenter.loadOffices()
        }

        recyclerViewOffices.layoutManager = LinearLayoutManager(context)
    }

    override fun officeChosen(office: Office) {
        presenter.officeChosenByUser(office)
    }

    override fun makeProgressBar(animate: Boolean) {
        swipeRefreshLayoutOffice.isRefreshing = animate
    }

    override fun onDestroyView() {
        presenter.onDestroyCalled()
        super.onDestroyView()
    }

    override fun setRecyclerViewOffices(offices: ArrayList<Office>) {
        activity!!.runOnUiThread {
            recyclerViewOffices.adapter = ChooseOfficeItemAdapter(offices, this)

            swipeRefreshLayoutOffice.isRefreshing = false
        }
    }

    override fun sayErrorToUser(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            swipeRefreshLayoutOffice.isRefreshing = false
        }
    }

    override fun closeFragment() {
        remote.calledCloseByOffice()
    }

}