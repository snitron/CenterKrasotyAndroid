package com.nitronapps.centerkrasoty.ui.view

import android.content.Intent
import android.os.Bundle
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Place
import com.nitronapps.centerkrasoty.model.PreOrder
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.about.view.AboutFragment
import com.nitronapps.centerkrasoty.ui.chooseOffice.view.ChooseOfficeFragment
import com.nitronapps.centerkrasoty.ui.chooseService.view.ChooseServiceFragment
import com.nitronapps.centerkrasoty.ui.login.view.LoginActivity
import com.nitronapps.centerkrasoty.ui.main.presenter.FragmentType
import com.nitronapps.centerkrasoty.ui.main.presenter.MainPresenter
import com.nitronapps.centerkrasoty.ui.main.presenter.TransactionStatus
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


interface MainView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun callSuperOnBackPressed()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFragmentByStatus(
        status: TransactionStatus,
        service: Service?,
        arrayList: ArrayList<PreOrder>?
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFragmentByType(type: FragmentType)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setItemOnBottomNavigationView(i: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startLoginPage()
}

interface MainFragmentRemote {
    fun calledCloseByFragment(status: TransactionStatus)

    fun calledCloseByConfirm()

    fun calledCloseByPlace(place: Place, service: Service)

    fun calledCloseByServices(serviceArr: Array<Service>)

    fun calledCloseByTime(service: Service, time: Long)

    fun calledCloseByUserInfo()


    fun calledBackByPlace()

    fun calledBackByTime()


    fun logoutAndStartLogin()
}

class MainActivity: MvpAppCompatActivity(R.layout.activity_main),
    MainView,
    MainFragmentRemote {
    private val presenter by moxyPresenter { MainPresenter(applicationContext) }
    lateinit var tmpFragment: MvpAppCompatFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.itemInfo -> {
                    presenter.invokedByBottomNavigationView(FragmentType.ABOUT)
                }
            }
        }
    }

    override fun startLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun setFragmentByStatus(status: TransactionStatus,
                                     service: Service?,
                                     arrayList: ArrayList<PreOrder>?) {
        runOnUiThread {
            var replaceableFragment = MvpAppCompatFragment()

            when (status) {
                TransactionStatus.OFFICE ->
                    replaceableFragment = ChooseOfficeFragment(this)

                TransactionStatus.SERVICE ->
                    replaceableFragment = ChooseServiceFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, replaceableFragment)
                .commitNow()
        }
    }

    override fun calledCloseByFragment(status: TransactionStatus) {
        presenter.startByCondition(status)
    }

    override fun setFragmentByType(type: FragmentType) {
        runOnUiThread {
            var replaceableFragment = MvpAppCompatFragment()

            when (type) {
                FragmentType.ABOUT ->
                    replaceableFragment = AboutFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, replaceableFragment)
                .commitNow()
        }
    }

    override fun callSuperOnBackPressed() {
        runOnUiThread {
            super.onBackPressed()
        }
    }
}
