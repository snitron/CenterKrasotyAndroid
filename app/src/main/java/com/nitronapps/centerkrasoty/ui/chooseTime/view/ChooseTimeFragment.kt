package com.nitronapps.centerkrasoty.ui.chooseTime.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseTime.adapter.ChooseTimeAdapter
import com.nitronapps.centerkrasoty.ui.chooseTime.presenter.ChooseTimePresenter
import com.nitronapps.centerkrasoty.ui.view.MainFragmentRemote
import kotlinx.android.synthetic.main.fragment_choose_place.*
import kotlinx.android.synthetic.main.fragment_choose_time.*
import moxy.MvpAppCompatFragment
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import okhttp3.internal.notifyAll

interface ChooseTimeView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun clearRecyclerView()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun closeTime(time: Long)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setDateInEditText(date: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProgressBarEnabled(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRecyclerView(
        times: ArrayList<Pair<String, Long>>,
        availability: ArrayList<Boolean>
    )

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startDatePickerDialog(year: Int, month: Int, day: Int)
}

interface ChooseTimeRemote {
    fun timeChosen(time: Pair<String, Long>)
}

class ChooseTimeFragment(private val remote: MainFragmentRemote,
                         private val service: Service): MvpAppCompatFragment(R.layout.fragment_choose_time),
    ChooseTimeView,
    ChooseTimeRemote {

    private val presenter by moxyPresenter { ChooseTimePresenter(context!!, service) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_time, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewChooseTime.layoutManager = GridLayoutManager(context!!, 2)

        textViewDateChooseTime.setOnClickListener {
            presenter.startDatePicker()
        }

        imageButtonChooseTime.setOnClickListener {
            presenter.startDatePicker()
        }

        toolbarChooseTime.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbarChooseTime.setNavigationOnClickListener {
            remote.calledBackByTime()
        }

        textViewServiceInfoValueChooseTime.text = service.name
    }

    override fun sayError(text: String) {
        activity!!.runOnUiThread {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun clearRecyclerView() {
        activity!!.runOnUiThread {
            recyclerViewChooseTime.layoutManager = null
        }
    }

    override fun closeTime(time: Long) {
        remote.calledCloseByTime(service, time)
    }

    override fun setDateInEditText(date: String) {
        activity!!.runOnUiThread {
            textViewDateChooseTime.text = date
        }
    }

    override fun setProgressBarEnabled(by: Boolean) {
        activity!!.runOnUiThread {
            progressBarChooseTime.visibility = if (by) View.VISIBLE else View.GONE
        }
    }

    override fun setRecyclerView(
        times: ArrayList<Pair<String, Long>>,
        availability: ArrayList<Boolean>
    ) {
        activity!!.runOnUiThread {
            recyclerViewChooseTime.layoutManager = GridLayoutManager(context!!, 2)
            recyclerViewChooseTime.adapter = ChooseTimeAdapter(
                times,
                availability,
                context!!,
                this
            )
        }
    }

    override fun startDatePickerDialog(year: Int, month: Int, day: Int) {
        activity!!.runOnUiThread {
            DatePickerDialog(
                context!!,
                DatePickerDialog.OnDateSetListener { datePicker, yearNew, monthNew, dayNew ->
                    if (presenter.checkToday(yearNew, monthNew, dayNew))
                        Toast.makeText(
                            context,
                            getString(R.string.youCantChoosePreviousDate),
                            Toast.LENGTH_SHORT
                        ).show()
                    else
                        presenter.newDateChosen(yearNew, monthNew, dayNew)
                },
                year,
                month,
                day
            ).show()
        }
    }

    override fun onDestroyView() {
        presenter.onDestroyCalled()
        super.onDestroyView()
    }

    override fun timeChosen(time: Pair<String, Long>) {
        presenter.timeChosen(time)
    }
}