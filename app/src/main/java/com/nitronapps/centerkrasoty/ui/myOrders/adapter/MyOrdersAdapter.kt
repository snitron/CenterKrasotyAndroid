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
        holder.textViewPrice.text = ""

        holder.imageButtonDelete.setOnClickListener {
            remote.userChosenToDelete(orders[position])
        }
    }

    class MyOrdersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.rootView.textViewItemConfirmServiceName!!
        val textViewDuration = itemView.rootView.textViewItemConfirmServiceDuration!!
        val textViewPrice = itemView.rootView.textViewItemConfirmServicePrice!!

        val imageButtonDelete = itemView.rootView.imageButtonItemConfirmDeleteItem
        val imageView = itemView.rootView.imageViewItemConfirmService
    }
}