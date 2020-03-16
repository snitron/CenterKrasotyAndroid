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
    Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.item_service
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textViewNameService.text = service.name

        if (service.info == ".")
            viewHolder.itemView.textViewInfoService.visibility = View.GONE
        else {
            viewHolder.itemView.textViewInfoService.visibility = View.VISIBLE
            viewHolder.itemView.textViewInfoService.text = service.info
        }
        viewHolder.itemView.textViewPriceService.text = String.format(Locale("ru", "RU") ,"%.2f", service.price).plus(" \u20BD")
        viewHolder.itemView.checkBoxService.isEnabled = initChecked || editable

        viewHolder.itemView.checkBoxService.setOnCheckedChangeListener { _, b ->
            remote.checkedService(service, b)
        }

        viewHolder.itemView.cardViewService.setOnClickListener {
            viewHolder.itemView.checkBoxService.isChecked = !viewHolder.itemView.checkBoxService.isChecked
        }
    }
}