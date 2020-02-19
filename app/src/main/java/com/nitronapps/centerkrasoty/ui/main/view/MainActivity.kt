package com.nitronapps.centerkrasoty.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.chooseOffice.view.ChooseOfficeFragment
import com.nitronapps.centerkrasoty.ui.login.view.LoginActivity
import com.nitronapps.centerkrasoty.ui.main.presenter.MainPresenter
import com.nitronapps.centerkrasoty.ui.main.presenter.TransactionStatus
import moxy.InjectViewState
import moxy.MvpAppCompatActivity
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MainView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startLoginPage()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setFragmentByStatus(status: TransactionStatus)


}

interface MainFragmentRemote{
    fun calledCloseByFragment(status: TransactionStatus)
}

class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainView, MainFragmentRemote {
    private val presenter by moxyPresenter { MainPresenter(applicationContext) }
    lateinit var tmpFragment: MvpAppCompatFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun startLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun setFragmentByStatus(status: TransactionStatus) {
        var replaceableFragment = MvpAppCompatFragment()

        when(status){
            TransactionStatus.OFFICE ->
                replaceableFragment = ChooseOfficeFragment(this)
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, replaceableFragment)
            .commitNow()
    }

    override fun calledCloseByFragment(status: TransactionStatus) {
        presenter.startByCondition(status)
    }
}
