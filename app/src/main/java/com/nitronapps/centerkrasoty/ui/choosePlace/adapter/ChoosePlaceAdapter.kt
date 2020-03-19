package com.nitronapps.centerkrasoty.ui.choosePlace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Place
import com.nitronapps.centerkrasoty.ui.choosePlace.view.ChoosePlaceRemote
import com.nitronapps.centerkrasoty.utils.SERVER_ADDRESS
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_place.view.*

class ChoosePlaceAdapter(private val places: Array<Place>,
                         private val remote: ChoosePlaceRemote): RecyclerView.Adapter<ChoosePlaceAdapter.ChoosePlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoosePlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)

        return ChoosePlaceViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: ChoosePlaceViewHolder, position: Int) {
        holder.textView.text = places[position].info

        holder.cardView.setOnClickListener {
            remote.placeChosen(places[position])
        }

        Picasso.get()
            .load(SERVER_ADDRESS.plus(places[position].image))
            .resize(200, 600)
            .centerCrop()
            .placeholder(R.drawable.ic_placeholder_300_200)
            .into(holder.imageView)
    }

    class ChoosePlaceViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        val textView = itemView.rootView.textViewPlaceName!!
        val cardView = itemView.rootView.cardViewPlace!!
        val imageView = itemView.rootView.imageViewPlaceImage!!
    }
}