package com.nitronapps.centerkrasoty.ui.myOrders.adapter

import com.nitronapps.centerkrasoty.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_my_orders_header.view.*

class MyOrdersHeader(val name: String): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item_my_orders_header
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textViewMyOrdersHeader.text = name
    }

}