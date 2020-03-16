package com.nitronapps.centerkrasoty.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Place
import com.nitronapps.centerkrasoty.model.PreOrder
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.about.view.AboutFragment
import com.nitronapps.centerkrasoty.ui.chooseOffice.view.ChooseOfficeFragment
import com.nitronapps.centerkrasoty.ui.choosePlace.view.ChoosePlaceFragment
import com.nitronapps.centerkrasoty.ui.chooseService.view.ChooseServiceFragment
import com.nitronapps.centerkrasoty.ui.chooseTime.view.ChooseTimeFragment
import com.nitronapps.centerkrasoty.ui.confirm.view.ConfirmFragment
import com.nitronapps.centerkrasoty.ui.login.view.LoginActivity
import com.nitronapps.centerkrasoty.ui.main.presenter.FragmentType
import com.nitronapps.centerkrasoty.ui.main.presenter.MainPresenter
import com.nitronapps.centerkrasoty.ui.main.presenter.TransactionStatus
import com.nitronapps.centerkrasoty.ui.myAccount.view.MyAccountFragment
import com.nitronapps.centerkrasoty.ui.myOrders.view.MyOrdersFragment
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
        service: Service? = null,
        preOrders: ArrayList<PreOrder>? = null
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFragmentByType(type: FragmentType)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setItemOnBottomNavigationView(id: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startLoginPage()
}

interface MainFragmentRemote {
    fun calledCloseByOffice()

    fun calledCloseByConfirm()

    fun calledCloseByPlace(place: Place, service: Service)

    fun calledCloseByServices(services: ArrayList<Service>)

    fun calledCloseByTime(service: Service, time: Long)

    fun calledCloseByMyAccount()


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

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.itemCreateNewOrder ->
                    presenter.invokedByBottomNavigationView(FragmentType.CREATE_ORDER,
                        R.id.itemCreateNewOrder)

                R.id.itemMyOrders ->
                    presenter.invokedByBottomNavigationView(FragmentType.MY_ORDERS,
                        R.id.itemMyOrders)

                R.id.itemMyAccount ->
                    presenter.invokedByBottomNavigationView(FragmentType.MY_ACCOUNT,
                        R.id.itemMyAccount)

                R.id.itemInfo ->
                    presenter.invokedByBottomNavigationView(FragmentType.ABOUT,
                        R.id.itemInfo)

                else ->
                    true
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
                                     preOrders: ArrayList<PreOrder>?) {
        runOnUiThread {
            var replaceableFragment = MvpAppCompatFragment()

            when (status) {
                TransactionStatus.OFFICE ->
                    replaceableFragment = ChooseOfficeFragment(this)

                TransactionStatus.SERVICE ->
                    replaceableFragment = ChooseServiceFragment(this)

                TransactionStatus.PLACE ->
                    replaceableFragment = ChoosePlaceFragment(this, service!!)

                TransactionStatus.TIME ->
                    replaceableFragment = ChooseTimeFragment(this, service!!)

                TransactionStatus.CONFIRM ->
                    replaceableFragment = ConfirmFragment(this, preOrders!!)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, replaceableFragment)
                .commitNow()
        }
    }

    override fun calledCloseByOffice() {
        presenter.calledCloseFromOffice()
    }

    override fun setFragmentByType(type: FragmentType) {
        runOnUiThread {
            var replaceableFragment = MvpAppCompatFragment()

            when (type) {
                FragmentType.ABOUT ->
                    replaceableFragment = AboutFragment()

                FragmentType.MY_ORDERS ->
                    replaceableFragment = MyOrdersFragment()

                FragmentType.MY_ACCOUNT ->
                    replaceableFragment = MyAccountFragment(this)
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

    override fun calledBackByPlace() {
        presenter.calledBackFromChoosePlace()
    }

    override fun calledBackByTime() {
        presenter.calledBackFromChooseTime()
    }

    override fun calledCloseByConfirm() {
        presenter.calledCloseByConfirm()
    }

    override fun calledCloseByMyAccount() {
        presenter.calledCloseByMyAccount()
    }

    override fun calledCloseByPlace(place: Place, service: Service) {
        presenter.calledCloseFromChoosePlace(place, service)
    }

    override fun calledCloseByServices(services: ArrayList<Service>) {
        presenter.calledCloseFromServices(services)
    }

    override fun calledCloseByTime(service: Service, time: Long) {
        presenter.calledInvokeChoosePlaceForTime(service, time)
    }

    override fun logoutAndStartLogin() {
        presenter.logout()
    }

    override fun setItemOnBottomNavigationView(id: Int) {
        runOnUiThread {
            bottomNavigationView.selectedItemId = id
        }
    }
}
