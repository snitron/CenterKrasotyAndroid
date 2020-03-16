package com.nitronapps.centerkrasoty.ui.chooseTime.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.chooseTime.view.ChooseTimeRemote
import kotlinx.android.synthetic.main.item_time.view.*

class ChooseTimeAdapter(private val times: ArrayList<Pair<String, Long>>,
                        private val ability: ArrayList<Boolean>,
                        private val context: Context,
                        private val remote: ChooseTimeRemote): RecyclerView.Adapter<ChooseTimeAdapter.ChooseTimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseTimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time, parent, false)

        return ChooseTimeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return times.size
    }

    override fun onBindViewHolder(holder: ChooseTimeViewHolder, position: Int) {
        holder.textViewTime.text = times[position].first

        if(ability[position]) {
            holder.constraintLayout.background = ContextCompat.getDrawable(context, R.drawable.button_orange_curved)
            holder.cardViewTime.setOnClickListener {
                remote.timeChosen(times[position])
            }
            holder.textViewTime.setTextColor(Color.WHITE)
        } else {
            holder.constraintLayout.background = ContextCompat.getDrawable(context, R.drawable.button_grey_curved_filled)
            holder.cardViewTime.setOnClickListener {
                Toast.makeText(context, context.getString(R.string.noFreeTimes), Toast.LENGTH_SHORT).show()
            }
            holder.textViewTime.setTextColor(Color.BLACK)
        }
    }

    class ChooseTimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewTime = itemView.rootView.textViewChooseTimeItem!!
        val cardViewTime = itemView.rootView.cardViewChooseTime!!
        val constraintLayout = itemView.rootView.constraintLayoutChooseTimeInner!!
    }
}