package com.nitronapps.centerkrasoty.ui.chooseService.adapter

import android.view.View
import com.nitronapps.centerkrasoty.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_service_header.view.*


class ChooseServiceGroupItem(val title: String): Item<ChooseServiceGroupItem.ChooseServiceGroupViewHolder>(){

    override fun getLayout(): Int { return R.layout.item_service_header }

    override fun bind(viewHolder: ChooseServiceGroupViewHolder, position: Int) {
        viewHolder.textViewTitle.text = title
    }

    class ChooseServiceGroupViewHolder(itemView: View): GroupieViewHolder(itemView) {
        val textViewTitle = itemView.textViewHeaderServiceTitle
    }
}