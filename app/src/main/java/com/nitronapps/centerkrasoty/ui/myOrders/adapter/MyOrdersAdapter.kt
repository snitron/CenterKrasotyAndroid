package com.nitronapps.centerkrasoty.ui.myOrders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Order
import com.nitronapps.centerkrasoty.model.Place
import com.nitronapps.centerkrasoty.model.PreOrder
import com.nitronapps.centerkrasoty.ui.confirm.view.ConfirmRemote
import com.nitronapps.centerkrasoty.ui.myOrders.view.MyOrdersRemote
import com.nitronapps.centerkrasoty.utils.SERVER_ADDRESS
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_order.view.*

class MyOrdersAdapter(val orders: ArrayList<Order>,
                      val remote: MyOrdersRemote): RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return MyOrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        holder.textViewName.text = orders[position].serviceName
        holder.textViewDuration.text = orders[position].getTimeRange()
            .plus(" ").plus(orders[position].getDay())
        holder.textViewPrice.text = ""
        holder.textViewPlace.text = orders[position].placeName

        holder.imageButtonDelete.setOnClickListener {
            remote.userChosenToDelete(orders[position])
        }

        Picasso.get()
            .load(SERVER_ADDRESS.plus(orders[position].placeImage))
            .resize(80, 80)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_80_80)
            .into(holder.imageView)
    }

    class MyOrdersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.rootView.textViewItemConfirmServiceName!!
        val textViewDuration = itemView.rootView.textViewItemConfirmServiceDuration!!
        val textViewPrice = itemView.rootView.textViewItemConfirmServicePrice!!
        val textViewPlace = itemView.rootView.textViewItemOrderPlace!!

        val imageButtonDelete = itemView.rootView.imageButtonItemConfirmDeleteItem!!
        val imageView = itemView.rootView.imageViewItemConfirmService!!
    }
}