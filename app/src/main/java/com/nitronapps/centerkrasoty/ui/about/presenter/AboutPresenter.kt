package com.nitronapps.centerkrasoty.ui.about.presenter

import com.nitronapps.centerkrasoty.ui.about.interactor.AboutInteractor
import com.nitronapps.centerkrasoty.ui.about.interactor.AboutInteractorInterface
import com.nitronapps.centerkrasoty.ui.about.view.AboutView
import moxy.MvpPresenter

class AboutPresenter: MvpPresenter<AboutView>() {
    private lateinit var interactor: AboutInteractorInterface

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = AboutInteractor()
    }
}