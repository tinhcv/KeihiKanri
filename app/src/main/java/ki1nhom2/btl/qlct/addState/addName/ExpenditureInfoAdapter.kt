package ki1nhom2.btl.qlct.addState.addName

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ki1nhom2.btl.qlct.MainActivity.Companion.toMoneyFormat
import ki1nhom2.btl.qlct.R

class ExpenditureInfoAdapter(private val eList: List<ExpenditureInfoNode>) : RecyclerView.Adapter<ExpenditureInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_node_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val expenditureInfo = eList[position]

        holder.expenditureName.text = expenditureInfo.consumptionType
        holder.expenditureCost.text = toMoneyFormat(expenditureInfo.consumptionCost)
        holder.checkBox.isChecked = expenditureInfo.checkBox;

        holder.checkBox.setOnClickListener {
            expenditureInfo.checkBox = holder.checkBox.isChecked
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return eList.size
    }

    class ViewHolder(ItemView : View) : RecyclerView.ViewHolder(ItemView) {
        val expenditureName : TextView = itemView.findViewById(R.id.expenditureName)
        val expenditureCost : TextView = itemView.findViewById(R.id.expenditureCost)
        val checkBox : CheckBox = itemView.findViewById(R.id.expenditureCheckBox)
    }
}