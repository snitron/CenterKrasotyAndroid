package com.nitronapps.centerkrasoty.ui.chooseService.view

import android.R.attr.data
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseService.adapter.ChooseServiceGroupItem
import com.nitronapps.centerkrasoty.ui.chooseService.adapter.ChooseServiceItem
import com.nitronapps.centerkrasoty.ui.chooseService.presenter.ChooseServicePresenter
import com.nitronapps.centerkrasoty.ui.view.MainFragmentRemote
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
import kotlin.concurrent.thread
import kotlin.jvm.internal.Intrinsics


interface ChooseServiceView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeFragmentByRemote(services: ArrayList<Service>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setButtonContinueEnabled(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFirstRecyclerView(map: Map<Int, ArrayList<Service>>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerViewAgain(
        map: Map<Int, ArrayList<Service>>,
        chosenGroups: ArrayList<Int>,
        chosenIds: ArrayList<Int>
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerViewRefreshing(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateTotalValue(value: String)
}

interface ChooseServiceRemote {
    fun checkedService(service: Service, state: Boolean)
}

class ChooseServiceFragment(private val remote: MainFragmentRemote):
    MvpAppCompatFragment(R.layout.fragment_choose_service),
    ChooseServiceView,
    ChooseServiceRemote {
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

        swipeRefreshServices.setOnRefreshListener {
            presenter.reloadServicesFromServer()
        }

        buttonServiceContinue.setOnClickListener {
            presenter.confirmButtonClicked()
        }
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun sayError(text: String) {
        Toast.makeText(context!!, text, Toast.LENGTH_SHORT).show()
    }

    override fun checkedService(service: Service, state: Boolean) {
        presenter.checkedServiceByUser(service, state)
    }

    override fun updateTotalValue(value: String) {
        activity!!.runOnUiThread {
            textViewTotalValue.text = value
        }
    }

    override fun setRecyclerViewRefreshing(by: Boolean) {
        activity!!.runOnUiThread {
            swipeRefreshServices.isRefreshing = true
        }
    }

    override fun closeFragmentByRemote(services: ArrayList<Service>) {
        remote.calledCloseByServices(services)
    }

    override fun setRecyclerViewAgain(
        map: Map<Int, ArrayList<Service>>,
        chosenGroups: ArrayList<Int>,
        chosenIds: ArrayList<Int>
    ) {
        thread {
            val sections = ArrayList<Section>()

            for ((_, value) in map) {
                val section = Section()
                section.setHeader(
                    ExpandableGroup(
                        ChooseServiceGroupItem(
                            map.values.first().first().groupName
                        )
                    )
                )

                for (i in value) {
                    val initChecked: Boolean =
                        chosenIds.contains(i.id)

                    val editable: Boolean =
                        !chosenGroups.contains(i.groupId)

                    section.add(ChooseServiceItem(i, this, initChecked, editable))
                }

                sections.add(section)
            }

            activity!!.runOnUiThread {
                setRecyclerViewRefreshing(true)
                groupAdapter.clear()
                groupAdapter.addAll(sections)
                groupAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun setFirstRecyclerView(map: Map<Int, ArrayList<Service>>) {
        thread {
            val sections = ArrayList<Section>()

            for((_, value) in map) {
                val section = Section()
                section.setHeader(
                    ExpandableGroup(
                        ChooseServiceGroupItem(
                            map.values.first().first().groupName
                        )
                    )
                )

                for (i in value)
                    section.add(ChooseServiceItem(i, this, false, true))

                sections.add(section)
            }

            activity!!.runOnUiThread {
                setRecyclerViewRefreshing(true)
                groupAdapter.clear()
                groupAdapter.addAll(sections)
                groupAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun setButtonContinueEnabled(by: Boolean) {
        activity!!.runOnUiThread {
            buttonServiceContinue.isEnabled = by
        }
    }
}