package ki1nhom2.btl.qlct.addState

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import ki1nhom2.btl.qlct.MainActivity
import ki1nhom2.btl.qlct.R
import ki1nhom2.btl.qlct.addState.addCost.ExpenditureCostNode
import ki1nhom2.btl.qlct.addState.addName.ExpenditureInfoAdapter
import ki1nhom2.btl.qlct.addState.addName.ExpenditureInfoNode
import ki1nhom2.btl.qlct.homeState.HomeStateActivity
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.balance
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.balanceDisplay
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.incomePerMonth
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.monthNames
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.outcomePerMonth
import java.util.*


class AddStateActivity : MainActivity() {

    companion object {
        // Add Name
/**
 * Danh sách các loại khoản chi
 */
        var consumptionTypeList : ArrayList<String> = ArrayList()
/**
 * Danh sách chi phí đã tiêu tốn cho khoản chi
 */
        var consumptionCostList : ArrayList<Long> = ArrayList()

        // Add Cost
        var consumptionInfoList : ArrayList<ExpenditureCostNode> = ArrayList()

        val data = ArrayList<ExpenditureInfoNode>()
        val calendar: Calendar = Calendar.getInstance()
    }

    lateinit var expenditureList : RecyclerView
    lateinit var message : TextView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_choose_state)

        changeColor(5)
    }

    //Thêm một khoản chi mới
    fun toAddExpenditureCost(view: View) {
        setContentView(R.layout.add_state_cost)
        message = findViewById(R.id.expenditureNameMessage)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateDisplay : TextView = findViewById(R.id.dateDisplay)
                dateDisplay.text = sdf.format(calendar.time)
            }

        val btnChooseDate : Button = findViewById(R.id.btnChooseDate)
        btnChooseDate.setOnClickListener {
            DatePickerDialog(this@AddStateActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Tạo Spinner chọn loại khoản chi
        var expenditureTypeChoosing = ""
        var expenditureIndexChoosing = 0
        val expenditureTypeSpinner : Spinner = findViewById(R.id.expenditureTypeSpinner)
        val adapterSpinner : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, consumptionTypeList)
        adapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        expenditureTypeSpinner.adapter = adapterSpinner

        // In ra thông tin của mục được chọn và vị trí
        expenditureTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                expenditureTypeChoosing = ""
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                expenditureTypeChoosing = consumptionTypeList[position]
                expenditureIndexChoosing = position
            }
        }

        val confirmButton : Button = findViewById(R.id.confirmButton)
        confirmButton.setOnClickListener {
            val expenditureNameTextField : TextInputEditText = findViewById(R.id.expenditureNameTextField)
            val expenditureCostTextField : TextInputEditText = findViewById(R.id.expenditureCostTextField)
            val dateDisplay : TextView = findViewById(R.id.dateDisplay)
            val description : TextInputEditText = findViewById(R.id.description)

            if(expenditureNameTextField.text?.isEmpty() == true) {
                message.text = "Nhập tên khoản chi!!"
            }
            else if(expenditureCostTextField.text?.isEmpty() == true) {
                message.text = "Nhập số tiền đã chi!!"
            }
            else if(dateDisplay.text?.isEmpty() == true) {
                message.text = "Vui lòng chọn ngày!!"
            }
            else if(expenditureCostTextField.text?.toString()?.toLong()!! > balance) {
                message.text = "Đã vượt quá ngân sách hiện có (${toMoneyFormat(balance)})! "
            }

            else {
                consumptionInfoList.add(
                    ExpenditureCostNode(
                        expenditureNameTextField.text.toString(),
                        expenditureCostTextField.text.toString().toLong(),
                        expenditureTypeChoosing,
                        dateDisplay.text as String,
                        description.text.toString()
                    )
                )

                data[expenditureIndexChoosing].consumptionCost += expenditureCostTextField.text.toString().toLong()
                consumptionCostList[expenditureIndexChoosing] += expenditureCostTextField.text.toString().toLong()
                balance -= expenditureCostTextField.text.toString().toLong()

                val monthChange: Int = consumptionInfoList.last().date.subSequence(3, 5).toString().toInt()
                outcomePerMonth[monthChange - 1] += consumptionInfoList.last().cost
                HomeStateActivity.data[monthChange - 1].outcome = outcomePerMonth[monthChange - 1]

                message.text = "Đã lưu thông tin khoản chi!"
                expenditureNameTextField.text?.clear()
                expenditureCostTextField.text?.clear()
                dateDisplay.text = ""
                description.text?.clear()

                balanceDisplay.text = toMoneyFormat(balance)

                saveData()
            }
        }
    }
    //Thêm các loại khoản chi
    fun toAddExpenditureName(view: View) {
        setContentView(R.layout.add_state_name)
        message = findViewById(R.id.expenditureNameMessage)

        expenditureList = findViewById(R.id.expenditureInfoList)
        expenditureList.layoutManager = LinearLayoutManager(this)

        val adapter = ExpenditureInfoAdapter(data)
        expenditureList.adapter = adapter

        val deleteBtn : ImageButton = findViewById(R.id.expenditureDelete)
        deleteBtn.setOnClickListener {
            for(i in data.size-1 downTo 0) {
                if(data[i].checkBox) {
                    data.removeAt(i)
                    consumptionTypeList.removeAt(i)
                    consumptionCostList.removeAt(i)
                    adapter.notifyItemRemoved(i)
                    message.text = "Xóa loại khoản chi thành công"
                    saveData()
                }
            }
        }

        val confirmButton : Button = findViewById(R.id.confirmButton)
        confirmButton.setOnClickListener {
            val nameInputted = findViewById<TextInputEditText>(R.id.expenditureNameInputField)
            if(nameInputted.text.toString().isNotEmpty() && !consumptionTypeList.contains(nameInputted.text.toString())) {
                consumptionTypeList.add(nameInputted.text.toString())
                consumptionCostList.add(0)
                data.add(
                    ExpenditureInfoNode(
                        consumptionTypeList.last(), consumptionCostList.last())
                )
                adapter.notifyItemInserted(consumptionTypeList.size-1)
                message.text = "Thêm loại khoản chi thành công"
                nameInputted.text = null

                saveData()
            }
            else if(consumptionTypeList.contains(nameInputted.text.toString())) {
                message.text = "Loại khoản chi đã tồn tại!"
            }
        }
    }

    //Thêm ngân sách
    fun toAddBudget(view: View) {
        setContentView(R.layout.add_state_budget)
        message = findViewById(R.id.expenditureNameMessage)

        val monthSpinner = findViewById<Spinner>(R.id.monthSpinner)
        val moneyInput = findViewById<TextInputEditText>(R.id.moneyInput)
        val description = findViewById<TextView>(R.id.description)
        val confirmBtn = findViewById<MaterialButton>(R.id.confirmButton)

        var monthIndex = 0
        val adapterSpinner : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, monthNames)
        adapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        monthSpinner.adapter = adapterSpinner

        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                monthIndex = position
            }
        }

        moneyInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                if(moneyInput.text?.isNotEmpty() == true) {
                    description.text = "Số dư tài khoản mới: ${
                        toMoneyFormat(
                            balance + moneyInput.text.toString().toLong()
                        )
                    }"
                }
            }
        })

        confirmBtn.setOnClickListener {
            balance += moneyInput.text.toString().toLong()
            incomePerMonth[monthIndex] += moneyInput.text.toString().toLong()
            HomeStateActivity.data[monthIndex].income += moneyInput.text.toString().toLong()
            HomeStateActivity.adapter.notifyItemChanged(monthIndex)
            message.text = "Đã cập nhật số dư!"
            moneyInput.text?.clear()
            description.text = ""

            balanceDisplay.text = toMoneyFormat(balance)

            saveData()
        }
    }

    fun backToAddState(view: View) {
        setContentView(R.layout.add_choose_state)

        saveData()
    }

}