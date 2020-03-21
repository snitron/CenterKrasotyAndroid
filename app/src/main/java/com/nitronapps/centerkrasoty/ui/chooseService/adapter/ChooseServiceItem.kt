package com.nitronapps.centerkrasoty.ui.chooseService.adapter

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.model.Service
import com.nitronapps.centerkrasoty.ui.chooseService.view.ChooseServiceRemote
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.item_service.view.*
import kotlinx.coroutines.processNextEventInCurrentThread
import java.util.*

class ChooseServiceItem(
    private val service: Service,
    private val remote: ChooseServiceRemote
) :
    Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.item_service
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        remote.registerCellById(id, service.id, service.groupId)

        viewHolder.itemView.textViewNameService.text = service.name

        if (service.info == ".")
            viewHolder.itemView.textViewInfoService.visibility = View.GONE
        else {
            viewHolder.itemView.textViewInfoService.visibility = View.VISIBLE
            viewHolder.itemView.textViewInfoService.text = service.info
        }
        viewHolder.itemView.textViewPriceService.text =

            String.format(Locale("ru", "RU"), "%.2f", service.price).plus(" \u20BD")

        viewHolder.itemView.checkBoxService.setOnCheckedChangeListener(null)

        viewHolder.itemView.checkBoxService.isChecked = remote.getChecked(id)

        viewHolder.itemView.checkBoxService.setOnCheckedChangeListener { it, b ->
                if ((remote.getPossibilityOfEditing(id) || !b)) {
                    remote.checkedService(service, b)
                } else
                    it.isChecked = !it.isChecked //Changing to "FALSE" state
        }


        viewHolder.itemView.cardViewService.setOnClickListener {
            if (remote.getPossibilityOfEditing(id) ||
                viewHolder.itemView.checkBoxService.isChecked
            )
                viewHolder.itemView.checkBoxService.isChecked =
                    !viewHolder.itemView.checkBoxService.isChecked
        }
    }
}