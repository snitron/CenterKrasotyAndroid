package com.nitronapps.centerkrasoty.ui.chooseService.adapter

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_service.view.*

class ChooseServiceItem(private val service:Service): Item<ChooseServiceItem.ChooseServiceViewHolder>(){
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
        viewHolder.textViewPrice.text = service.price.toString().format("%.2f").plus(" \u20BD")
    }

    class ChooseServiceViewHolder(itemView: View): GroupieViewHolder(itemView) {
        val textViewName: TextView = itemView.textViewNameService
        val textViewInfo: TextView = itemView.textViewNameService
        val textViewPrice: TextView = itemView.textViewPriceService
        val cardView: CardView = itemView.cardViewService
    }
}