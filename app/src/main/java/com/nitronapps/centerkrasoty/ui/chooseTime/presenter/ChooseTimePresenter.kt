package com.nitronapps.centerkrasoty.ui.chooseTime.presenter

import android.content.Context
import com.google.gson.Gson
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Order
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseTime.interactor.ChooseTimeInteractor
import com.nitronapps.centerkrasoty.ui.chooseTime.interactor.ChooseTimeInteractorInterface
import com.nitronapps.centerkrasoty.ui.chooseTime.view.ChooseTimeView
import com.nitronapps.centerkrasoty.utils.getPlused
import com.nitronapps.centerkrasoty.utils.trueAfter
import com.nitronapps.centerkrasoty.utils.trueBefore
import moxy.MvpPresenter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.jvm.internal.Intrinsics

class ChooseTimePresenter(val context: Context,
                          val service: Service) : MvpPresenter<ChooseTimeView>() {
    private lateinit var interactor: ChooseTimeInteractorInterface

    private var currentDate = getCalendar().time
    private var count = 0
    private val precompiledPlaces = ArrayList<Pair<Long, ArrayList<Int>>>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        interactor = ChooseTimeInteractor(this)
        viewState.setDateInEditText(getPrettyDate())
        viewState.setProgressBarEnabled(true)

        interactor.prepareUserAndOfficeAndGetOrders(service.groupId, getServerDate())
    }

    fun sayError() {
        viewState.sayError(
            context.getString(R.string.serverError)
        )
    }

    fun sayDBError() {
        viewState.sayError(
            context.getString(R.string.dbError)
        )
    }

    fun ordersGot(orders: ArrayList<Order>) {
        thread {
            val parsedOrders = mutableMapOf<Int, ArrayList<Order>>().withDefault { arrayListOf() }

            orders.forEach {
                if (parsedOrders[it.placeId] == null)
                    parsedOrders[it.placeId] = arrayListOf()

                parsedOrders[it.placeId]!!.add(it)
            }

            val formatterServer = SimpleDateFormat("yyyy-MM-dd", Locale("ru", "RU"))
            val formatterHours = SimpleDateFormat("HH:mm", Locale("ru", "RU"))

            var startTime = interactor.getOffice().getStartTimeParsed(formatterServer.format(currentDate))
            val finishTime = interactor.getOffice().getFinishTimeParsed(formatterServer.format(currentDate))

            val times = arrayListOf<Pair<String, Long>>()
            val abilities = arrayListOf<Boolean>()

            while(finishTime.trueAfter(startTime)) {
                val serviceFinishDate = startTime.getPlused(service.long * 60000)

                var ability = true
                val listOfPlaces = arrayListOf<Int>()

                parsedOrders.forEach {
                    val tmpAbility = it.value.any {
                        checkInabilityOfPlace(
                            orderStart = it.getStartTimeParsed(),
                            orderFinish = it.getFinishTimeParsed(),
                            startTime = startTime,
                            serviceFinishTime = serviceFinishDate
                        )
                    }

                    ability = ability && tmpAbility

                    if (tmpAbility) {
                        listOfPlaces.add(it.key)
                    }
                }

                abilities.add(!ability || parsedOrders.size != count)
                times.add(Pair(formatterHours.format(startTime), startTime.time))
                precompiledPlaces.add(Pair(startTime.time, listOfPlaces))

                startTime.time += 1800000

                if(!finishTime.after(startTime.getPlused(service.long * 60000)))
                    break
            }

            viewState.setProgressBarEnabled(false)
            viewState.setRecyclerView(
                times = times,
                availability = abilities
            )
        }
    }

    fun onDestroyCalled() {
        interactor.disposeRequests()
    }

    fun checkToday(year: Int, month: Int, day: Int): Boolean {
        val calendar = getCalendar()
        return !(calendar.get(Calendar.YEAR) < year ||
                calendar.get(Calendar.MONTH) < month ||
                calendar.get(Calendar.DAY_OF_MONTH) <= day)
    }

    fun getPrettyDate(): String {
        return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(this.currentDate)
    }

    private fun getServerDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this.currentDate)
    }

    private fun getCalendar(): Calendar {
        return Calendar.getInstance()
    }

    fun newDateChosen(year: Int, month: Int, day: Int) {
        val calendarNew = Calendar.getInstance()
        calendarNew.set(year, month, day)

        currentDate = calendarNew.time

        viewState.clearRecyclerView()
        viewState.setProgressBarEnabled(true)
        viewState.setDateInEditText(getPrettyDate())

        interactor.getOrders(service.groupId, getServerDate())
    }

    fun startDatePicker(){
        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        viewState.startDatePickerDialog(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun timeChosen(time: Pair<String, Long>) {
        interactor.setUnavailablePlaces(
            precompiledPlaces.first { it.first == time.second }.second
        )

        viewState.closeTime(time.second)
    }

    private fun checkInabilityOfPlace(
        orderStart: Date,
        orderFinish: Date,
        startTime: Date,
        serviceFinishTime: Date
    ): Boolean {
        return orderStart.trueAfter(startTime) && orderFinish.trueBefore(serviceFinishTime) ||
                orderStart.trueBefore(startTime) && orderFinish.trueAfter(startTime) ||
                orderStart.trueBefore(serviceFinishTime) && orderFinish.trueAfter(serviceFinishTime) ||
                orderStart == startTime && orderFinish == serviceFinishTime
    }

}