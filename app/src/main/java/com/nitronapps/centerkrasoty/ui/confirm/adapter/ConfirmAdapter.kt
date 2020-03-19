package com.nitronapps.centerkrasoty.ui.confirm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Place
import com.nitronapps.centerkrasoty.model.PreOrder
import com.nitronapps.centerkrasoty.ui.confirm.view.ConfirmRemote
import com.nitronapps.centerkrasoty.utils.SERVER_ADDRESS
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_order.view.*

class ConfirmAdapter(val preOrders: ArrayList<PreOrder>,
                     val remote: ConfirmRemote): RecyclerView.Adapter<ConfirmAdapter.ConfirmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ConfirmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return preOrders.size
    }

    override fun onBindViewHolder(holder: ConfirmViewHolder, position: Int) {
        holder.textViewName.text = preOrders[position].service.name
        holder.textViewDuration.text = preOrders[position].getDurationPretty()
            .plus(" ").plus(preOrders[position].getDay())
        holder.textViewPrice.text = preOrders[position].service
            .price.toString().format("%.2f").plus(" \u20BD")
        holder.textViewPlace.text = preOrders[position].place.info

        Picasso.get()
            .load(SERVER_ADDRESS.plus(preOrders[position].place.image))
            .resize(80, 80)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_80_80)
            .into(holder.imageView)

        holder.imageButtonDelete.setOnClickListener {
            remote.deleteItem(preOrders[position])
        }
    }

    class ConfirmViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.rootView.textViewItemConfirmServiceName!!
        val textViewDuration = itemView.rootView.textViewItemConfirmServiceDuration!!
        val textViewPrice = itemView.rootView.textViewItemConfirmServicePrice!!
        val textViewPlace = itemView.rootView.textViewItemOrderPlace!!

        val imageButtonDelete = itemView.rootView.imageButtonItemConfirmDeleteItem!!
        val imageView = itemView.rootView.imageViewItemConfirmService!!
    }
}