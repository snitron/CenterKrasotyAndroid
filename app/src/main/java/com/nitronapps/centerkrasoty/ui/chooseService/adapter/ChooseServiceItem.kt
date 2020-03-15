package com.nitronapps.centerkrasoty.ui.chooseService.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseService.view.ChooseServiceRemote
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_service.view.*
import java.util.*

class ChooseServiceItem(private val service: Service,
                        private val remote: ChooseServiceRemote,
                        private val initChecked: Boolean,
                        private val editable: Boolean) :
    Item<ChooseServiceItem.ChooseServiceViewHolder>() {
    private val compositeDisposable = CompositeDisposable()

    override fun getLayout(): Int {
        return R.layout.item_service
    }

    override fun bind(viewHolder: ChooseServiceViewHolder, position: Int) {
        viewHolder.textViewName.text = service.name
        if (service.info == ".")
            viewHolder.textViewInfo.visibility = View.GONE
        else {
            viewHolder.textViewInfo.visibility = View.VISIBLE
            viewHolder.textViewInfo.text = service.info
        }
        viewHolder.textViewPrice.text = String.format(Locale("ru", "RU") ,"%.2f", service.price).plus(" \u20BD")
        viewHolder.checkBox.isEnabled = initChecked || editable

        viewHolder.checkBox.setOnCheckedChangeListener { _, b ->
            remote.checkedService(service, b)
        }

        viewHolder.cardView.setOnTouchListener { _, _ ->
            viewHolder.checkBox.isChecked = !viewHolder.checkBox.isChecked
            return@setOnTouchListener true
        }
    }

    class ChooseServiceViewHolder(itemView: View) : GroupieViewHolder(itemView) {
        val textViewName: TextView = itemView.textViewNameService
        val textViewInfo: TextView = itemView.textViewNameService
        val textViewPrice: TextView = itemView.textViewPriceService
        val checkBox: CheckBox = itemView.checkBoxService
        val cardView: CardView = itemView.cardViewService
    }
}