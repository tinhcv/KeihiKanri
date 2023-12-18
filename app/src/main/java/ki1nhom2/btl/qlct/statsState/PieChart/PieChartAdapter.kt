package ki1nhom2.btl.qlct.statsState.PieChart

import android.graphics.Color
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionCostList
import ki1nhom2.btl.qlct.addState.AddStateActivity.Companion.consumptionTypeList
import java.text.DecimalFormat
import kotlin.random.Random

class PieChartAdapter {

    companion object {

        private fun setDataForPieChart(pieChart : PieChart) {
            val entries: ArrayList<PieEntry> = ArrayList()

            for(i in 0 until consumptionTypeList.size) {
                entries.add(PieEntry(consumptionCostList[i].toFloat(), consumptionTypeList[i]))
            }
            // Tạo đối tượng PieDataSet va set thuộc tính
            val dataSet = PieDataSet(entries, "")
            dataSet.setDrawIcons(false)
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0F, 40F)
            dataSet.selectionShift = 5f

            // add colors
            val colors: ArrayList<Int> = ArrayList()

            for(i in 0 until entries.size)
                colors.add(Color.rgb(Random.nextInt(0, 255), Random.nextInt(0, 255), Random.nextInt(0, 255)))

            dataSet.colors = colors

            // Tạo đối tượng PieData và cài đặt các thuộc tính
            //dataSet.setSelectionShift(0f);
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(11f)
            data.setValueTextColor(Color.WHITE)
            data.setValueFormatter(PercentageDisplay())

            pieChart.data = data

            // undo all highlights
            pieChart.highlightValues(null)
            pieChart.invalidate()
        }

        fun renderData(pieChart: PieChart) {
            pieChart.setUsePercentValues(true)
            pieChart.description.isEnabled = false
            pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

            pieChart.dragDecelerationFrictionCoef = 0.95f

            pieChart.isDrawHoleEnabled = true
            pieChart.setHoleColor(Color.WHITE)

            pieChart.setTransparentCircleColor(Color.WHITE)
            pieChart.setTransparentCircleAlpha(110)

            pieChart.holeRadius = 58f
            pieChart.transparentCircleRadius = 61f

            pieChart.setDrawCenterText(true)

            pieChart.rotationAngle = 0.toFloat()
            // enable rotation of the chart by touch
            pieChart.isRotationEnabled = true
            pieChart.isHighlightPerTapEnabled = true


            pieChart.animateY(1400, Easing.EaseInOutQuad)

            pieChart.spin(2000, 0F, 360F, Easing.EaseInOutQuad)
            val l = pieChart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)
            l.xEntrySpace = 7f
            l.yEntrySpace = 0f
            l.yOffset = 0f

            // entry label styling
            pieChart.setEntryLabelColor(Color.WHITE)
            pieChart.setEntryLabelTextSize(12f)

            setDataForPieChart(pieChart)
        }

        internal class PercentageDisplay : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val df = DecimalFormat("#.##").format(value)
                return "$df%"
            }
        }
    }
}