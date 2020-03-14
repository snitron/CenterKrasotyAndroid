package com.nitronapps.centerkrasoty.ui.about.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nitronapps.centerkrasoty.R
import moxy.MvpAppCompatFragment
import moxy.MvpView

interface AboutView : MvpView {

}

class AboutFragment : MvpAppCompatFragment(R.layout.fragment_about), AboutView {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }
}