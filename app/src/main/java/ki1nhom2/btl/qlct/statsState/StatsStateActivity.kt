package ki1nhom2.btl.qlct.statsState

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.button.MaterialButton
import ki1nhom2.btl.qlct.MainActivity
import ki1nhom2.btl.qlct.R
import ki1nhom2.btl.qlct.addState.AddStateActivity
import ki1nhom2.btl.qlct.homeState.HomeStateActivity.Companion.outcomePerMonth
import ki1nhom2.btl.qlct.statsState.LineChart.LineChartAdapter
import ki1nhom2.btl.qlct.statsState.PieChart.PieChartAdapter
import kotlin.math.floor


class StatsStateActivity : MainActivity() {

    private lateinit var lineChart : LineChart
    private lateinit var pieChart : PieChart

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stats_state)

        val title = findViewById<TextView>(R.id.title)
        lineChart = findViewById(R.id.lineChart)
        pieChart = findViewById(R.id.pieChart)

        val btnShowType = findViewById<MaterialButton>(R.id.showTypeGraph)
        val btnShowMonth = findViewById<MaterialButton>(R.id.showMonthGraph)

        val typeList : ArrayList<String> = ArrayList()
        typeList.add("")
        typeList.addAll(AddStateActivity.consumptionTypeList)

        val typeLinearLayout = findViewById<LinearLayout>(R.id.typeLinearLayout)
        typeLinearLayout.visibility = View.INVISIBLE

        var typeChoosing = ""
        val typeSpinner : Spinner = findViewById(R.id.typeSpinner)
        val adapterSpinner : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeList)
        adapterSpinner.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        typeSpinner.adapter = adapterSpinner

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                typeChoosing = ""
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                typeChoosing = typeList[position]

                lineChart.clear()
                LineChartAdapter.renderData(lineChart, typeChoosing)
            }
        }

        btnShowType.setOnClickListener {
            title.text = "Biến động chi tiêu qua các tháng"
            lineChart.visibility = View.VISIBLE
            pieChart.visibility = View.GONE

            lineChart.setTouchEnabled(true)

            LineChartAdapter.renderData(lineChart, typeChoosing)

            btnShowType.isClickable = false
            btnShowMonth.isClickable = true

            typeLinearLayout.visibility = View.VISIBLE
        }

        btnShowMonth.setOnClickListener {
            title.text = "Tỉ lệ các loại khoản chi"
            lineChart.visibility = View.GONE
            pieChart.visibility = View.VISIBLE

            pieChart.setTouchEnabled(true)

            PieChartAdapter.renderData(pieChart)

            btnShowType.isClickable = true
            btnShowMonth.isClickable = false

            typeLinearLayout.visibility = View.INVISIBLE
        }

        changeColor(3)
    }
}