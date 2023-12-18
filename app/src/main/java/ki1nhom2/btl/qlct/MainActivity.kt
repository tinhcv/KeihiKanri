package ki1nhom2.btl.qlct

import android.content.Intent
import com.google.gson.Gson
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ki1nhom2.btl.qlct.addState.AddStateActivity
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionInfoList
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionCostList
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionTypeList
import ki1nhom2.btl.qlct.homeState.HomeStateActivity
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.balance
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.data
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.incomePerMonth
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.monthNames
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.outcomePerMonth
import ki1nhom2.btl.qlct.homeState.monthlyShorten.MonthlyInfoNode
import ki1nhom2.btl.qlct.profileState.ProfileStateActivity
import ki1nhom2.btl.qlct.statsState.StatsStateActivity
import ki1nhom2.btl.qlct.transactionsState.TransactionStateActivity
import com.google.gson.reflect.TypeToken
import ki1nhom2.btl.qlct.addState.addCost.ExpenditureCostNode
import ki1nhom2.btl.qlct.addState.addName.ExpenditureInfoNode
import java.lang.reflect.Type

open class MainActivity : AppCompatActivity() {

    companion object {

        // variables

        var isInited : Boolean = false
        var firstTime : Boolean = true

        // functions

        fun toMoneyFormat(money : Long) : String {
            if(money < 1000) return money.toString()

            var input : Long = money
            var output = ""

            var counter = 0
            while (input > 0) {
                if(counter % 3 == 0 && counter != 0) output = ",$output"
                output = (input % 10).toString() + output
                input /= 10
                counter++
            }

            return output
        }

        fun toLongFormat(money : String) : Long {
            var output : Long = 0

            for(c in money) {
                if(c != ',')
                    output = output * 10 + c.code
            }

            return output
        }

        fun recalculateMoney() {
            balance = 0
            for(record in consumptionInfoList) {
                outcomePerMonth[record.date.substring(3,5).toInt()] += record.cost
            }
            for(i in 0 until 12) {
                balance += incomePerMonth[i] - outcomePerMonth[i]
            }
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_state)

        loadData()

        if(firstTime) {
            createDefaultData()
            firstTime = false
        }

        if(!isInited) {
            for(i in 1..12) {
                data.add(
                    MonthlyInfoNode(
                        monthNames[i - 1],
                        incomePerMonth[i - 1],
                        outcomePerMonth[i - 1]
                    )
                )
            }
            for (i in 0 until consumptionTypeList.size) {
                AddStateActivity.data.add(
                    ExpenditureInfoNode(
                        consumptionTypeList[i], consumptionCostList[i]
                    )
                )
            }
            isInited = true
        }
    }

    fun changeColor(contentIndex : Int) {
        val btnHome = findViewById<ImageButton>(R.id.btnHome)
        val btnTrans = findViewById<ImageButton>(R.id.btnTrans)
        val btnStats = findViewById<ImageButton>(R.id.btnStats)
        val btnProfile = findViewById<ImageButton>(R.id.btnProfile)

        val txtHome = findViewById<TextView>(R.id.txtHome)
        val txtTrans = findViewById<TextView>(R.id.txtTrans)
        val txtStats = findViewById<TextView>(R.id.txtStats)
        val txtProfile = findViewById<TextView>(R.id.txtProfile)

        val btnAdd = findViewById<ImageButton>(R.id.btnAdd)

        val listBtn : ArrayList<ImageButton> = arrayListOf(btnHome, btnTrans, btnStats, btnProfile)
        val listTxt : ArrayList<TextView> = arrayListOf(txtHome, txtTrans, txtStats, txtProfile)

        if(contentIndex == 5) {
            btnAdd.isClickable = false
            btnAdd.setColorFilter(Color.argb(100, 5, 128, 60))
        }
        else {
            listBtn[contentIndex-1].isClickable = false
            listBtn[contentIndex-1].setColorFilter(Color.BLACK)
            listTxt[contentIndex-1].setTextColor(Color.BLACK)
        }
    }

    private fun createDefaultData() {
        for(i in 1..12) {
            incomePerMonth.add(0)
            outcomePerMonth.add(0)
        }

        consumptionTypeList.add("Tiền nhà")
        consumptionTypeList.add("Tiền điện")
        consumptionTypeList.add("Tiền nước")

        consumptionCostList.add(0)
        consumptionCostList.add(0)
        consumptionCostList.add(0)
    }

    fun toHomeState(view: View) {
        val intent = Intent(this, HomeStateActivity::class.java)
        startActivity(intent)
    }
    fun toTransactionState(view: View) {
        val intent = Intent(this, TransactionStateActivity::class.java)
        startActivity(intent)
    }
    fun toAddState(view: View) {
        val intent = Intent(this, AddStateActivity::class.java)
        startActivity(intent)
    }
    fun toStatsState(view: View) {
        val intent = Intent(this, StatsStateActivity::class.java)
        startActivity(intent)
    }
    fun toProfileState(view: View) {
        val intent = Intent(this, ProfileStateActivity::class.java)
        startActivity(intent)
    }

    fun saveData() {
        val pref = getSharedPreferences("database", MODE_PRIVATE)

        pref.edit().putString("firstTime", Gson().toJson(firstTime)).apply()

        pref.edit().putString("balance", Gson().toJson(balance)).apply()
        pref.edit().putString("allIncome", Gson().toJson(incomePerMonth)).apply()
        pref.edit().putString("allOutcome", Gson().toJson(outcomePerMonth)).apply()

        pref.edit().putString("allExpenditureName", Gson().toJson(consumptionTypeList)).apply()
        pref.edit().putString("allExpenditureCost", Gson().toJson(consumptionCostList)).apply()
        pref.edit().putString("allConsumptionInfo", Gson().toJson(consumptionInfoList)).apply()
    }

    fun loadData() {
        val pref = getSharedPreferences("database", MODE_PRIVATE)
        var jsonData = pref.getString("firstTime", null)
        firstTime = jsonData == null

        jsonData = pref.getString("balance", null)
        if(jsonData != null)
            balance = Gson().fromJson(jsonData,Long::class.java)
        else
            balance = 0

        jsonData = pref.getString("allIncome", null)
        if(jsonData != null) {
            val type: Type = object : TypeToken<ArrayList<Long?>?>() {}.type
            incomePerMonth = Gson().fromJson(jsonData, type) as ArrayList<Long>
        }
        else
            incomePerMonth = ArrayList()

        jsonData = pref.getString("allOutcome", null)
        if(jsonData != null) {
            val type: Type = object : TypeToken<ArrayList<Long?>?>() {}.type
            outcomePerMonth = Gson().fromJson(jsonData, type) as ArrayList<Long>
        }
        else
            outcomePerMonth = ArrayList()

        jsonData = pref.getString("allExpenditureName", null)
        if(jsonData != null) {
            val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
            consumptionTypeList = Gson().fromJson(jsonData, type) as ArrayList<String>
        }
        else
            consumptionTypeList = ArrayList()

        jsonData = pref.getString("allExpenditureCost", null)
        if(jsonData != null) {
            val type: Type = object : TypeToken<ArrayList<Long?>?>() {}.type
            consumptionCostList = Gson().fromJson(jsonData, type) as ArrayList<Long>
        }
        else
            consumptionCostList = ArrayList()

        jsonData = pref.getString("allConsumptionInfo", null)
        if(jsonData != null) {
            val type: Type = object : TypeToken<ArrayList<ExpenditureCostNode?>?>() {}.type
            consumptionInfoList = Gson().fromJson(jsonData, type) as ArrayList<ExpenditureCostNode>
        }
        else
            consumptionInfoList = ArrayList()
    }
}