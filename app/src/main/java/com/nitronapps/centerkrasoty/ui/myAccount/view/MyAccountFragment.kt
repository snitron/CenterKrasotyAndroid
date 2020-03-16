package com.nitronapps.centerkrasoty.ui.myAccount.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.ui.myAccount.presenter.MyAccountPresenter
import com.nitronapps.centerkrasoty.ui.view.MainFragmentRemote
import com.nitronapps.centerkrasoty.utils.withFirstUpperLetter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_my_account.*
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType


interface MyAccountView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setOffice(office: Office, url: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProgressbarEnabled(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSwitchState(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setUser(name: String, surname: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeMyAccountAndChangeOffice()
}

class MyAccountFragment(val remote: MainFragmentRemote): MvpAppCompatFragment(R.layout.fragment_choose_time),
        MyAccountView {

    private val presenter by moxyPresenter { MyAccountPresenter(context!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        switchNotification.setOnCheckedChangeListener { _, b ->
            presenter.switchChanged(b)
        }

        toolbarMyAccount.inflateMenu(R.menu.user_info_menu)
        toolbarMyAccount.setOnMenuItemClickListener{

            AlertDialog.Builder(context)
                .setTitle(R.string.areYouSureToLogout)
                .setPositiveButton(R.string.logout) { dialog, _ -> remote.logoutAndStartLogin(); dialog.dismiss() }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                .show()

            return@setOnMenuItemClickListener true
        }

        buttonSettingsOfficeChange.setOnClickListener {
            remote.calledCloseByMyAccount()
        }
    }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun setUser(name: String, surname: String) {
        activity!!.runOnUiThread {
            textViewUserNameMyAccount.text = "${surname.withFirstUpperLetter()} ${name.withFirstUpperLetter()}"
        }
    }

    override fun setOffice(office: Office, url: String) {
        activity!!.runOnUiThread {
            textViewSettingsOfficeName.text = office.name
            textViewSettingsOfficeAddress.text = office.address

            Picasso.get().load(url)
                .resize(300, 200)
                .centerCrop()
                .into(imageViewMyAccount)
        }
    }

    override fun setProgressbarEnabled(by: Boolean) {
        activity!!.runOnUiThread {
            progressBarMyAccount.visibility = if (by) View.VISIBLE else View.GONE
        }
    }

    override fun setSwitchState(by: Boolean) {
        activity!!.runOnUiThread {
            switchNotification.isChecked = by
        }
    }

    override fun closeMyAccountAndChangeOffice() {
        remote.calledCloseByMyAccount()
    }


}