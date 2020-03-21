import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.chooseService.view.ChooseServiceRemote
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.item_service_header.view.*

import kotlin.jvm.internal.Intrinsics
import kotlin.math.exp

class ChooseServiceGroupItem(
    private val title: String) : Item<GroupieViewHolder>(),
    ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun getLayout(): Int = R.layout.item_service_header

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val textView =
            viewHolder.itemView.textViewHeaderServiceTitle

        viewHolder.itemView.cardViewServiceHeader.setOnClickListener {
            expandableGroup.onToggleExpanded()

            if(expandableGroup.isExpanded)
                ViewCompat.animate(viewHolder.itemView.imageViewServiceHeaderArrow)
                    .setDuration(250)
                    .rotation(-180.0f)
                    .start()
            else
                ViewCompat.animate(viewHolder.itemView.imageViewServiceHeaderArrow)
                    .setDuration(250)
                    .rotation(0f)
                    .start()
        }

        textView.text = title
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        this.expandableGroup = onToggleListener
    }

}