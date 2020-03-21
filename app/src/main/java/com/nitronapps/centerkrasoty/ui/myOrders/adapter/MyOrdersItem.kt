package com.nitronapps.centerkrasoty.ui.myOrders.adapter

import android.view.View
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Order
import com.nitronapps.centerkrasoty.ui.myOrders.view.MyOrdersRemote
import com.nitronapps.centerkrasoty.utils.SERVER_ADDRESS
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_order.view.*

class MyOrdersItem(val order: Order,
                   val isFuture: Boolean,
                   val remote: MyOrdersRemote): Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.item_order
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textViewItemConfirmServiceName.text = order.serviceName
        viewHolder.itemView.textViewItemConfirmServiceDate.text = order.getTimeRange()
            .plus(" ").plus(order.getDay())
        viewHolder.itemView.textViewItemConfirmServicePrice.text = ""
        viewHolder.itemView.textViewItemOrderPlace.text = order.placeName

        if(isFuture) {
            viewHolder.itemView.imageButtonItemConfirmDeleteItem.visibility = View.VISIBLE
            viewHolder.itemView.imageButtonItemConfirmDeleteItem.setOnClickListener {
                remote.userChosenToDelete(order)
            }
        } else {
            viewHolder.itemView.imageButtonItemConfirmDeleteItem.visibility = View.INVISIBLE
            viewHolder.itemView.imageButtonItemConfirmDeleteItem.setOnClickListener(null)
        }

        Picasso.get()
            .load(SERVER_ADDRESS.plus(order.placeImage))
            .resize(80, 80)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_80_80)
            .into(viewHolder.itemView.imageViewItemConfirmService)
    }
}