package com.nitronapps.centerkrasoty.ui.choosePlace.presenter

import android.content.Context
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Order
import com.nitronapps.centerkrasoty.model.Place
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.choosePlace.interactor.ChoosePlaceInteractor
import com.nitronapps.centerkrasoty.ui.choosePlace.interactor.ChoosePlaceInteractorInterface
import com.nitronapps.centerkrasoty.ui.choosePlace.view.ChoosePlaceView
import moxy.MvpPresenter

class ChoosePlacePresenter(val context: Context,
                           val service: Service): MvpPresenter<ChoosePlaceView>() {
    private lateinit var interactor: ChoosePlaceInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ChoosePlaceInteractor(this)

        interactor.prapareUserAndGetPlaces(service.groupId)
    }

    fun sayError(){
        viewState.sayError(
            context.getString(R.string.serverError)
        )
    }

    fun getPlaces(){
        interactor.disposeRequests()
        interactor.getPlaces(service.groupId)
    }

    fun placesGot(places: ArrayList<Place>) {
        viewState.setRecyclerViewEnabled(true)
        viewState.setSwipeRefreshViewRefreshing(false)

        val unavailablePlaces = interactor.getListOfUnavailablePlaces()

        viewState.setRecyclerView(
            places.filter { !unavailablePlaces.contains(it.id) }.toTypedArray()
        )
    }

    fun onDestroyCalled(){
        interactor.disposeRequests()
    }

    fun placeChosenByUser(place: Place) {
        viewState.closeChoosePlace(place, service)
    }

}