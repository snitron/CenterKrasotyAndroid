package com.nitronapps.centerkrasoty.ui.chooseOffice.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.data.entity.Office
import com.nitronapps.centerkrasoty.ui.chooseOffice.view.ChooseOfficeItemRemoteInterface
import com.squareup.picasso.Picasso

class ChooseOfficeItemAdapter(
    private val offices: ArrayList<Office>,
    val remote: ChooseOfficeItemRemoteInterface
) : RecyclerView.Adapter<ChooseOfficeItemAdapter.ChooseOfficeItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseOfficeItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_office, parent, false)

        return ChooseOfficeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return offices.size
    }

    override fun onBindViewHolder(holder: ChooseOfficeItemViewHolder, position: Int) {
        Picasso.get()
            .load(prepareYandexStaticApi(offices[position].geoCoords))
            .resize(300, 200)
            .centerCrop()
            .into(holder.imageView)

        holder.textViewName.text = offices[position].name
        holder.textViewAddress.text = offices[position].address

        holder.buttonConfirm.setOnClickListener {
            remote.officeChosen(offices[position])
        }

    }

    private fun prepareYandexStaticApi(coords: String): String {
        return "https://static-maps.yandex.ru/1.x/?ll=" +
                coords.split(" ").reversed().joinToString(",") +
                "&size=450,300&z=17&l=map"

    }

    class ChooseOfficeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageViewOffice)
        val textViewName = itemView.findViewById<TextView>(R.id.textViewOfficeName)
        val textViewAddress = itemView.findViewById<TextView>(R.id.textViewOfficeAddress)
        val buttonConfirm = itemView.findViewById<Button>(R.id.buttonChooseOffice)
    }
}