package com.nitronapps.centerkrasoty.ui.confirm.presenter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.broadcast.NotificationBroadcastReciever
import com.nitronapps.centerkrasoty.model.PreOrder
import com.nitronapps.centerkrasoty.model.TransactionRequest
import com.nitronapps.centerkrasoty.ui.confirm.interactor.ConfirmInteractor
import com.nitronapps.centerkrasoty.ui.confirm.interactor.ConfirmInteractorInterface
import com.nitronapps.centerkrasoty.ui.confirm.view.ConfirmView
import moxy.MvpPresenter
import kotlin.concurrent.thread

class ConfirmPresenter(val context: Context,
                       val preOrders: ArrayList<PreOrder>): MvpPresenter<ConfirmView>() {
    private lateinit var interactor: ConfirmInteractorInterface
    private var preOrderToDelete: PreOrder? = null
    private var value = 0.0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ConfirmInteractor(this)

        preOrders.forEach { value += it.service.price }

        setValueToTextView()
        viewState.setRecyclerView(preOrders)
    }

    fun sayError(){
        viewState.sayError(
            context.getString(R.string.serverError)
        )
    }

    fun sayDBError() {
        viewState.sayError(
            context.getString(R.string.dbError)
        )
    }

    fun onDestroyCalled(){
        interactor.disposeRequests()
    }

    fun userChosenToDelete(preOrder: PreOrder) {
        preOrderToDelete = preOrder

        viewState.setDeleteDialog(preOrder.service.name)
    }

    fun userConfirmedDelete() {
        preOrders.remove(preOrderToDelete)
        value -= preOrderToDelete!!.service.price

        setValueToTextView()

        if(preOrders.size == 0)
            viewState.closeConfirm()
        else
            viewState.setRecyclerView(preOrders)
    }

    fun closeConfirm() {
        viewState.closeConfirm()
    }

    fun setValueToTextView() {
        viewState.setSumToTextView(value.toString().format("%.2f").plus(" \u20BD"))
    }

    fun userConfirmedPreOrders() {
        viewState.setProgressBarVisible(true)

        thread {
            interactor.prepareUserAndSendTransactions(
                preOrders.map {
                    TransactionRequest(
                        date = it.getDate(),
                        placeId = it.place.id,
                        groupId = it.service.groupId,
                        startTime = it.getStartDate(),
                        finishTime = it.getFinishTime())
                }
            )
        }
    }

    fun transactionSent() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        thread {

            if(interactor.getNotificationState()) {
                for(preOrder in preOrders) {
                    val intent = Intent(context, NotificationBroadcastReciever::class.java)
                    intent.putExtra("serviceName", preOrder.service.name)
                    intent.putExtra("isOrder", true)

                    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        preOrder.time - 3600000, //TODO: Time zone
                        pendingIntent
                    )
                }
            }

            viewState.setProgressBarVisible(false)
            viewState.showSuccessfulDialog()
        }
    }

}