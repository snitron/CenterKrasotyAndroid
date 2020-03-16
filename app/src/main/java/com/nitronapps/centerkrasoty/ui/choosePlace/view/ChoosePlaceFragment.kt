package com.nitronapps.centerkrasoty.ui.choosePlace.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Place
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.choosePlace.adapter.ChoosePlaceAdapter
import com.nitronapps.centerkrasoty.ui.choosePlace.presenter.ChoosePlacePresenter
import com.nitronapps.centerkrasoty.ui.view.MainFragmentRemote
import kotlinx.android.synthetic.main.fragment_choose_place.*
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


interface ChoosePlaceView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeChoosePlace(place: Place, service: Service)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerView(places: Array<Place>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerViewEnabled(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSwipeRefreshViewRefreshing(by: Boolean)
}

interface ChoosePlaceRemote {
    fun placeChosen(place: Place)
}

class ChoosePlaceFragment(private val remote: MainFragmentRemote,
                          private val service: Service): MvpAppCompatFragment(R.layout.fragment_choose_place),
        ChoosePlaceView,
        ChoosePlaceRemote{

    private val presenter by moxyPresenter { ChoosePlacePresenter(context!!, service) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewPlaces.layoutManager = LinearLayoutManager(context)

        swipeRefreshLayoutPlace.setOnRefreshListener {
            presenter.getPlaces()
        }

        toolbarChoosePlace.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbarChoosePlace.setNavigationOnClickListener {
            remote.calledBackByPlace()
        }

        textViewChoosePlaceServiceInfoValue.text = service.name
    }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun closeChoosePlace(place: Place, service: Service) {
        remote.calledCloseByPlace(place, service)
    }

    override fun placeChosen(place: Place) {
        presenter.placeChosenByUser(place)
    }

    override fun setRecyclerView(places: Array<Place>) {
        activity!!.runOnUiThread {
            recyclerViewPlaces.adapter = ChoosePlaceAdapter(places, this)
        }
    }

    override fun setRecyclerViewEnabled(by: Boolean) {
        activity!!.runOnUiThread {
            recyclerViewPlaces.isEnabled = by
        }
    }

    override fun setSwipeRefreshViewRefreshing(by: Boolean) {
        activity!!.runOnUiThread {
            swipeRefreshLayoutPlace.isRefreshing = by
        }
    }



    override fun onDestroyView() {
        presenter.onDestroyCalled()
        super.onDestroyView()
    }
}