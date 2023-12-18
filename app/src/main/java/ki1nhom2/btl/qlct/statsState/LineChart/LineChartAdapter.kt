package ki1nhom2.btl.qlct.statsState.LineChart

import android.graphics.Color
import android.graphics.DashPathEffect
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import ki1nhom2.btl.qlct.MainActivity
import ki1nhom2.btl.qlct.MainActivity.Companion.toMoneyFormat
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionInfoList
import ki1nhom2.btl.qlct.homeState.HomeStateActivity
import java.text.DecimalFormat
import kotlin.math.floor

class LineChartAdapter {

    companion object {

        private fun setDataForLineChart(lineChart: LineChart, typeChoosing : String) {
            val values: ArrayList<Entry> = ArrayList()
            for(i in 0 until 12) {
                if(typeChoosing == "")
                    values.add(Entry((i+1).toFloat(), HomeStateActivity.outcomePerMonth[i].toFloat()))
                else {
                    val specificOutcomePerMonth : ArrayList<Long> = ArrayList()
                    for(j in 0 until 12) {
                        specificOutcomePerMonth.add(0)
                    }
                    for(record in consumptionInfoList) {
                        if(record.type == typeChoosing) {
                            specificOutcomePerMonth[record.date.substring(3,5).toInt()-1] += record.cost
                        }
                    }
                    values.add(Entry((i + 1).toFloat(), specificOutcomePerMonth[i].toFloat()))
                }
            }
            val set1: LineDataSet
            if (lineChart.data != null && lineChart.data.dataSetCount > 0) {
                set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
                set1.values = values
                lineChart.data.notifyDataChanged()
                lineChart.notifyDataSetChanged()
            }
            else {
                set1 = LineDataSet(values, "Số tiền đã chi trong tháng")
                set1.setDrawCircles(true)
                set1.enableDashedLine(10f, 0f, 0f)
                set1.enableDashedHighlightLine(10f, 0f, 0f)
                set1.color = Color.rgb( 5, 128, 60)
                set1.setCircleColor(Color.rgb(5, 128, 60))
                set1.lineWidth = 2f //line size
                set1.circleRadius = 5f
                set1.setDrawCircleHole(true)
                set1.valueTextSize = 10f
    //            set1.setDrawFilled(true)
                set1.formLineWidth = 5f
                set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
                set1.formSize = 5f
                set1.fillColor = Color.WHITE
                set1.setDrawValues(true)
                val dataSets: ArrayList<ILineDataSet> = ArrayList()
//                set1.valueFormatter = ClaimsYAxisValueFormatter()
                dataSets.add(set1)
                val data = LineData(dataSets)
                lineChart.data = data


            }
        }

        fun renderData(lineChart: LineChart, typeChoosing : String) {
            val xAxisLabel: ArrayList<String> = ArrayList()
            for(i in 1 until 13) {
                xAxisLabel.add("$i")
            }
            val xAxis: XAxis = lineChart.xAxis
            val position = XAxis.XAxisPosition.BOTTOM
            xAxis.position = position
    //        xAxis.setDrawGridLinesBehindData(false)
            xAxis.setDrawGridLines(false)

    //        xAxis.enableGridDashedLine(2f, 7f, 0f)
    //        xAxis.axisMaximum = 5f
    //        xAxis.axisMinimum = 0f
            xAxis.setLabelCount(12, true)
    //        xAxis.isGranularityEnabled = true
    //        xAxis.granularity = 7f
    //        xAxis.labelRotationAngle = 315f
    //        xAxis.valueFormatter = ClaimsXAxisValueFormatter(dates)
            xAxis.setDrawLimitLinesBehindData(true)

            val leftAxis: YAxis = lineChart.axisLeft
            leftAxis.removeAllLimitLines()
            leftAxis.axisMaximum = floor(findMaximumValueInList() / 100000f) * 100000 + 500000
            leftAxis.axisMinimum = 0f
            leftAxis.enableGridDashedLine(10f, 10f, 0f)
            leftAxis.setDrawZeroLine(false)
            leftAxis.setDrawLimitLinesBehindData(false)
            leftAxis.valueFormatter = ClaimsYAxisValueFormatter()

            lineChart.axisRight.isEnabled = false
            lineChart.description.isEnabled = false

            setDataForLineChart(lineChart, typeChoosing)
        }

        private fun findMaximumValueInList() : Long {
            var output : Long = 0
            for(record in HomeStateActivity.outcomePerMonth) {
                if(record > output) {
                    output = record
                }
            }
            return output
        }

        //định dạng giá trị của biểu đồ đường, chuyển đổi số tiền thành chuỗi có định dạng tiền tệ.
        internal class ClaimsYAxisValueFormatter : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                val df = DecimalFormat("#").format(value)
                val money = df.toLong()
                if(money >= 1000000)
                    return toMoneyFormat(money).substring(0, toMoneyFormat(money).length - 6) + "tr"
                else
                    return toMoneyFormat(money)
            }
        }
    }
}