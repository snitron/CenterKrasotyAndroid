package com.nitronapps.centerkrasoty.ui.confirm.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.PreOrder
import com.nitronapps.centerkrasoty.ui.confirm.adapter.ConfirmAdapter
import com.nitronapps.centerkrasoty.ui.confirm.presenter.ConfirmPresenter
import kotlinx.android.synthetic.main.fragment_choose_service.*
import kotlinx.android.synthetic.main.fragment_confirm.*
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import okhttp3.internal.notifyAll


interface ConfirmView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeConfirm()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setDeleteDialog(serviceName: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProgressBarVisible(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerView(preOrders: ArrayList<PreOrder>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setSumToTextView(sum: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showSuccessfulDialog()
}

interface ConfirmRemote {
    fun deleteItem(preOrder: PreOrder)
}

class ConfirmFragment(preOrders: ArrayList<PreOrder>): MvpAppCompatFragment(R.layout.fragment_confirm),
        ConfirmView,
        ConfirmRemote {

    private val presenter by moxyPresenter { ConfirmPresenter(activity!!, preOrders) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewConfirm.layoutManager = LinearLayoutManager(context!!)
        buttonConfirm.setOnClickListener {
            presenter.userConfirmedPreOrders()
        }
    }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        presenter.onDestroyCalled()
        super.onDestroyView()
    }

    override fun deleteItem(preOrder: PreOrder) {
        presenter.userChosenToDelete(preOrder)
    }

    override fun setDeleteDialog(serviceName: String) {
        activity!!.runOnUiThread {
            AlertDialog.Builder(context!!)
                .setTitle(R.string.areYouSure)
                .setMessage(getString(R.string.areYouSureToDelete) + " $serviceName")
                .setPositiveButton(R.string.delete) { dialogInterface, _ ->  presenter.userConfirmedDelete(); dialogInterface.dismiss()}
                .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
                .show()
        }
    }

    override fun setProgressBarVisible(by: Boolean) {
        activity!!.runOnUiThread {
            progressBarConfirm.visibility = if (by) View.VISIBLE else View.GONE
        }
    }

    override fun setRecyclerView(preOrders: ArrayList<PreOrder>) {
        activity!!.runOnUiThread {
            recyclerViewConfirm.adapter = ConfirmAdapter(preOrders, this)
            recyclerViewConfirm.notifyAll()
        }
    }

    override fun setSumToTextView(sum: String) {
        activity!!.runOnUiThread {
            textViewConfirmSumValue.text = sum
        }
    }

    override fun showSuccessfulDialog() {
        activity!!.runOnUiThread {
            AlertDialog.Builder(context!!)
                .setTitle(R.string.orderCreated)
                .setPositiveButton(R.string.close) { dialogInterface, _ -> presenter.closeConfirm() }
                .show()
        }
    }

    override fun closeConfirm() {
        //TODO: Interaction with main
    }
}