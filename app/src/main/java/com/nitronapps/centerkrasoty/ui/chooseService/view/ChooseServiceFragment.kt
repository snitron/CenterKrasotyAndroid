package com.nitronapps.centerkrasoty.ui.chooseService.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseService.adapter.ChooseServiceGroupItem
import com.nitronapps.centerkrasoty.ui.chooseService.adapter.ChooseServiceItem
import com.nitronapps.centerkrasoty.ui.chooseService.presenter.ChooseServicePresenter
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_choose_service.*
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface ChooseServiceView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerView(data: Map<Int, ArrayList<Service>>)
}

class ChooseServiceFragment: MvpAppCompatFragment(R.layout.fragment_choose_service),
    ChooseServiceView {
    private val presenter by moxyPresenter { ChooseServicePresenter(context!!) }
    private lateinit var groupAdapter: GroupAdapter<GroupieViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_service, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipeRefreshServices.isRefreshing = true
        groupAdapter = GroupAdapter()

        recyclerViewServices.layoutManager = LinearLayoutManager(context)
        recyclerViewServices.adapter = groupAdapter

        swipeRefreshServices.setOnRefreshListener { presenter.getServicesFromServer() }
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun sayError(text: String) {
        Toast.makeText(context!!, text, Toast.LENGTH_SHORT).show()
    }

    override fun setRecyclerView(data: Map<Int, ArrayList<Service>>) {
        for(i in data){
            val group = ExpandableGroup(ChooseServiceGroupItem(i.value.first().groupName))

            for(j in i.value)
                group.add(ChooseServiceItem(j))

            groupAdapter.add(group)
        }

        activity!!.runOnUiThread {
            groupAdapter.notifyDataSetChanged()
            swipeRefreshServices.isRefreshing = false
        }
    }
}