package ki1nhom2.btl.qlct.transactionsState

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import ki1nhom2.btl.qlct.MainActivity
import ki1nhom2.btl.qlct.R
import ki1nhom2.btl.qlct.addState.AddStateActivity
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.calendar
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionCostList
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionInfoList
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionTypeList
import ki1nhom2.btl.qlct.addState.addCost.ExpenditureCostNode
import ki1nhom2.btl.qlct.homeState.HomeStateActivity
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.balance
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.balanceDisplay
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.outcomePerMonth
import java.util.*
import kotlin.collections.ArrayList

class TransactionStateActivity : MainActivity() {

    companion object {
        var consumptionChoosing : ExpenditureCostNode = ExpenditureCostNode("", 0, "", "", "")
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trans_state)

        changeColor(2)

/*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
* */

        val typeList : ArrayList<String> = ArrayList()
        typeList.add("")
        typeList.addAll(consumptionTypeList)

        var typeChoosing = ""
        val typeSpinner : Spinner = findViewById(R.id.typeSpinner)
        val typeAdapterSpinner : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeList)
        typeAdapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        typeSpinner.adapter = typeAdapterSpinner

        val monthList : ArrayList<String> = ArrayList()
        monthList.add("")
        monthList.addAll(HomeStateActivity.monthNames)

        var monthChoosing = -1
        val monthSpinner : Spinner = findViewById(R.id.monthSpinner)
        val monthAdapterSpinner : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, monthList)
        monthAdapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        monthSpinner.adapter = monthAdapterSpinner

        val consumptionList : ArrayList<ExpenditureCostNode> = ArrayList()
        val nameList : ArrayList<String> = ArrayList()
        nameList.add("")
        for(record in consumptionInfoList) {
            if(record.type == typeChoosing && record.date.substring(3,5).toInt() == monthChoosing) {
                consumptionList.add(record)
                nameList.add(record.name)
            }
        }

        val nameSpinner : Spinner = findViewById(R.id.nameSpinner)
        val cost = findViewById<TextInputEditText>(R.id.expenditureCostTextField)
        val date = findViewById<TextView>(R.id.dateDisplay)
        val description = findViewById<TextInputEditText>(R.id.description)

        val newTypeSpinner : Spinner = findViewById(R.id.newTypeSpinner)
        val newTypeAdapterSpinner : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeList)
        newTypeAdapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        newTypeSpinner.adapter = newTypeAdapterSpinner

        var nameAdapterSpinner : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, nameList)
        nameAdapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        nameSpinner.adapter = nameAdapterSpinner

/*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
* */

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                if(position == 0) {
                    typeChoosing = ""
                }
                else {
                    typeChoosing = consumptionTypeList[position - 1]
                    consumptionList.clear()
                    nameList.clear()
                    nameList.add("")
                    for(record in consumptionInfoList)
                        if(record.type == typeChoosing)
                            if(monthChoosing == 0) {
                                consumptionList.add(record)
                                nameList.add(record.name)
                            }
                            else if (monthChoosing == record.date.substring(3,5).toInt()) {
                                consumptionList.add(record)
                                nameList.add(record.name)
                            }
                    nameAdapterSpinner = ArrayAdapter(this@TransactionStateActivity, android.R.layout.simple_spinner_item, nameList)
                    nameAdapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
                    nameSpinner.adapter = nameAdapterSpinner
                }
        }

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                if(position == 0) {
                    monthChoosing = 0
                }
                else {
                    monthChoosing = position
                    consumptionList.clear()
                    nameList.clear()
                    nameList.add("")
                    for(record in consumptionInfoList)
                        if(record.date.substring(3,5).toInt() == monthChoosing)
                            if(typeChoosing == "") {
                                consumptionList.add(record)
                                nameList.add(record.name)
                            } else if (typeChoosing == record.type) {
                                consumptionList.add(record)
                                nameList.add(record.name)
                            }
                    nameAdapterSpinner = ArrayAdapter(this@TransactionStateActivity, android.R.layout.simple_spinner_item, nameList)
                    nameAdapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
                    nameSpinner.adapter = nameAdapterSpinner
                }
        }

        nameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0) {
                    consumptionChoosing = consumptionList[position - 1]
                    cost.setText(consumptionChoosing.cost.toString())
                    date.text = consumptionChoosing.date
                    newTypeSpinner.setSelection(consumptionTypeList.indexOf(consumptionChoosing.type)+1)
                    description.setText(consumptionChoosing.description)
                }
            }
        }

/*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*/

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                date.text = sdf.format(calendar.time)
            }

        val btnChooseDate : Button = findViewById(R.id.btnChooseDate)
        btnChooseDate.setOnClickListener {
            DatePickerDialog(this@TransactionStateActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

/*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*/

        var newTypeChoosing = ""

        newTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0) {
                    newTypeChoosing = consumptionTypeList[position - 1]
                }
            }
        }

        val actionList : ArrayList<String> = arrayListOf("", "Sửa", "Xoá")
        val actionSpinner = findViewById<Spinner>(R.id.actionSpinner)
        val actionAdapterSpinner : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, actionList)
        actionAdapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        actionSpinner.adapter = actionAdapterSpinner
        actionSpinner.setSelection(typeSpinner.selectedItemPosition)

        val message = findViewById<TextView>(R.id.message)

        actionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val index : Int = consumptionInfoList.indexOf(consumptionChoosing)
                var isModified = false
                if(position == 1) {
                    if(cost.text?.isEmpty() == true) {
                        message.text = "Nhập số tiền đã chi!!"
                    }
                    else if(cost.text?.toString()?.toLong()!! - consumptionInfoList[index].cost > balance) {
                        message.text = "Đã vượt quá ngân sách hiện có (${toMoneyFormat(balance)})! "
                    }
                    else {
                        balance += consumptionInfoList[index].cost - cost.text.toString().toLong()
                        consumptionCostList[consumptionTypeList.indexOf(consumptionChoosing.type)] += cost.text.toString().toLong() - consumptionInfoList[index].cost

                        AddStateActivity.data[consumptionTypeList.indexOf(consumptionChoosing.type)].consumptionCost -= consumptionInfoList[index].cost
                        outcomePerMonth[consumptionChoosing.date.substring(3,5).toInt() - 1] -= consumptionInfoList[index].cost
                        HomeStateActivity.data[consumptionChoosing.date.substring(3,5).toInt() - 1].outcome -= consumptionInfoList[index].cost

                        consumptionInfoList[index].type = newTypeChoosing
                        consumptionInfoList[index].cost = cost.text.toString().toLong()
                        consumptionInfoList[index].date = date.text.toString()
                        consumptionInfoList[index].description = description.text.toString()

                        AddStateActivity.data[consumptionTypeList.indexOf(newTypeChoosing)].consumptionCost += cost.text.toString().toLong()
                        outcomePerMonth[date.text.toString().substring(3,5).toInt() - 1] += cost.text.toString().toLong()
                        HomeStateActivity.data[date.text.toString().substring(3,5).toInt() - 1].outcome += cost.text.toString().toLong()

                        message.text = "Sửa khoản chi thành công!"

                        balanceDisplay.text = toMoneyFormat(balance)

                        isModified = true

                        saveData()
                    }
                }
                else if(position == 2) {
                    balance += consumptionInfoList[index].cost
                    consumptionCostList[consumptionTypeList.indexOf(newTypeChoosing)] += consumptionInfoList[index].cost

                    AddStateActivity.data[consumptionTypeList.indexOf(newTypeChoosing)].consumptionCost -= consumptionInfoList[index].cost
                    outcomePerMonth[consumptionChoosing.date.substring(3,5).toInt() - 1] -= consumptionInfoList[index].cost
                    HomeStateActivity.data[consumptionChoosing.date.substring(3,5).toInt() - 1].outcome -= consumptionInfoList[index].cost

                    consumptionInfoList.removeAt(index)

                    message.text = "Xoá khoản chi thành công!"

                    balanceDisplay.text = toMoneyFormat(balance)

                    isModified = true

                    saveData()
                }

                if(isModified) {
                    typeSpinner.setSelection(0)
                    monthSpinner.setSelection(0)
                    nameSpinner.setSelection(0)
                    cost.setText("")
                    date.text = ""
                    description.setText("")
                    newTypeSpinner.setSelection(0)
                }
                actionSpinner.setSelection(0)
            }
        }
    }
}