package ki1nhom2.btl.qlct.homeState.monthlyShorten

import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import ki1nhom2.btl.qlct.MainActivity.Companion.toMoneyFormat
import ki1nhom2.btl.qlct.R
import ki1nhom2.btl.qlct.addState.AddStateActivity
import ki1nhom2.btl.qlct.homeState.HomeStateActivity
import ki1nhom2.btl.qlct.homeState.PopUp

class MonthlyInfoAdapter(private val mList: List<MonthlyInfoNode>) : RecyclerView.Adapter<MonthlyInfoAdapter.ViewHolder>() {

    var type : Int = 0

    // Tạo views mới
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_node_monthly_info, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val monthlyStatisticStructure = mList[position]

        holder.monthName.text = monthlyStatisticStructure.monthName
        if(toMoneyFormat(monthlyStatisticStructure.income).length >= 9) {
            holder.income.text = toMoneyFormat(monthlyStatisticStructure.income)
                .substring(0, toMoneyFormat(monthlyStatisticStructure.income).length - 6) + "tr"
        }
        else holder.income.text = toMoneyFormat(monthlyStatisticStructure.income)

        if(toMoneyFormat(monthlyStatisticStructure.outcome).length >= 9) {
            holder.outcome.text = toMoneyFormat(monthlyStatisticStructure.outcome)
                .substring(0, toMoneyFormat(monthlyStatisticStructure.outcome).length - 6) + "tr"
        }
        else holder.outcome.text = toMoneyFormat(monthlyStatisticStructure.outcome)

        holder.itemView.setOnClickListener {
            if(type == 0) {
                generateConsumptionList(holder, holder.monthName.text as String)
                holder.dropDown.rotationX = 180f
            }
            else if(type == 1) {
                HomeStateActivity.data[position].consumptionList.clear()
                holder.list.removeAllViews()
                holder.dropDown.rotationX = 0f

                notifyItemChanged(position)
            }
            type = 1 - type
        }

        holder.dropDown.setOnClickListener {
            if(type == 0) {
                generateConsumptionList(holder, monthlyStatisticStructure.monthName)
                holder.dropDown.rotationX = 180f
            }
            else if(type == 1) {
                HomeStateActivity.data[position].consumptionList.clear()
                holder.list.removeAllViews()
                holder.dropDown.rotationX = 0f

                notifyItemChanged(position)
            }
            type = 1 - type
        }
    }

    private fun generateConsumptionList(holder : ViewHolder, monthName : String) {

        for(i in 0 until AddStateActivity.consumptionInfoList.size) {
            if(AddStateActivity.consumptionInfoList[i].date.substring(3, 5).toInt()-1 == HomeStateActivity.monthNames.indexOf(monthName)) {
                HomeStateActivity.data[HomeStateActivity.monthNames.indexOf(monthName)].consumptionList.add(AddStateActivity.consumptionInfoList[i])

                val child : View = LayoutInflater.from(holder.list.context).inflate(R.layout.home_consumption_details, holder.list, false)
                val name : TextView = child.findViewById(R.id.name)
                val type : TextView = child.findViewById(R.id.type)
                val cost : TextView = child.findViewById(R.id.cost)
                val btnSeeMore : MaterialButton = child.findViewById(R.id.btnSeeMore)

                name.text = AddStateActivity.consumptionInfoList[i].name
                type.text = AddStateActivity.consumptionInfoList[i].type
                cost.text = toMoneyFormat(AddStateActivity.consumptionInfoList[i].cost)

                btnSeeMore.setOnClickListener {
                    val popupWindow = PopUp()
                    popupWindow.showPopupWindow(it, AddStateActivity.consumptionInfoList[i])
                }

                holder.list.addView(child)
            }
        }
    }

    // Trả về số lượng các mục trong danh sách
    override fun getItemCount(): Int {
        return mList.size
    }

    // Giữ các chế độ xem để thêm nó vào hình ảnh và văn bản
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val monthName: TextView = itemView.findViewById(R.id.monthName)
        val income: TextView = itemView.findViewById(R.id.income)
        val outcome: TextView = itemView.findViewById(R.id.outcome)
        val dropDown : ImageButton = itemView.findViewById(R.id.dropdown)
        val list : LinearLayout = itemView.findViewById(R.id.consumptionList)
    }

}
