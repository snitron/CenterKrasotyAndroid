import android.view.View
import android.widget.TextView
import com.nitronapps.centerkrasoty.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.item_service_header.view.*

import kotlin.jvm.internal.Intrinsics

class ChooseServiceGroupItem(private val title: String) : Item<GroupieViewHolder>(),
    ExpandableItem {

    override fun getLayout(): Int = R.layout.item_service_header

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val textView =
            viewHolder.itemView.textViewHeaderServiceTitle

        textView.text = title
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        Intrinsics.checkParameterIsNotNull(onToggleListener, "onToggleListener")
    }

}