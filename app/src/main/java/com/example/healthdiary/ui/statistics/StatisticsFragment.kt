package com.example.healthdiary.ui.statistics

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import com.example.healthdiary.db.DBHelper
import com.example.healthdiary.db.UserParameters
import com.example.healthdiary.db.WaterTrackParameters
import com.example.healthdiary.util.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class StatisticsFragment : Fragment() {

    private val waterUsageLabel = "Употребление воды"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_statistics, container, false)
        val spinner = initSpinner(root)
        val parametersChart = root.findViewById<LineChart>(R.id.parameters_chart)

        val dbHelper = DBHelper(this.requireContext())
        val userParameterList = getSortedUserParameters(dbHelper)
        val waterTrackParametersList = getSortedWaterTrackParameters(dbHelper);

        val elements = resources.getStringArray(R.array.statistics_list)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var data: LineData? = null
                when (val selected = elements[position]) {
                    waterUsageLabel -> {
                        if (waterTrackParametersList.isNotEmpty()) {
                            data = mapWaterTrackingParametersToLineData(
                                waterTrackParametersList,
                                waterUsageLabel
                            )
                            parametersChart.xAxis.valueFormatter = object : ValueFormatter() {
                                override fun getAxisLabel(value: Float, axis: AxisBase?): String =
                                    waterTrackParametersList[value.toInt()].date
                            }
                        }
                    }
                    else -> {
                        if (userParameterList.isNotEmpty()) {
                            data = mapUserParametersToLineData(userParameterList, selected)
                            parametersChart.xAxis.valueFormatter = object : ValueFormatter() {
                                override fun getAxisLabel(value: Float, axis: AxisBase?): String =
                                    userParameterList[value.toInt()].date
                            }
                        }
                    }
                }
                if (data != null) {
                    parametersChart.data = data
                    parametersChart.invalidate()
                }
            }
        }
        parametersChart.setNoDataText("Нет данных")
        parametersChart.description = Description()
        parametersChart.description.text = ""
        parametersChart.setNoDataTextColor(Color.BLACK)
        parametersChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        parametersChart.invalidate()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSortedUserParameters(dbHelper: DBHelper) =
        dbHelper.getUserParametersList().sortedBy {
            LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSortedWaterTrackParameters(dbHelper: DBHelper) =
        dbHelper.getWaterTrackParametersList().sortedBy {
            LocalDate.parse(it.date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }

    private fun initSpinner(root: View): Spinner {
        val spinner = root.findViewById<Spinner>(R.id.chart_spinner)
        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.statistics_list,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        return spinner
    }

    private fun mapWaterTrackingParametersToLineData(
        waterTrackParameters: List<WaterTrackParameters>,
        parameter: String
    ): LineData {
        val data = ArrayList<ILineDataSet>()
        val entryList = waterTrackParameters
            .mapIndexed { i, it ->
                Entry(i.toFloat(), it.currentIntake.toFloat(), it.date)
            }.toList()
        val lineDataSet = toLineDataSet(entryList, parameter, Color.GREEN, 4F, 15f, Color.WHITE)
        data.add(lineDataSet)
        return LineData(data)
    }

    private fun mapUserParametersToLineData(
        userParameterList: List<UserParameters>,
        parameter: String
    ): LineData {
        val data = ArrayList<ILineDataSet>()
        when (parameter) {
            "Рост" -> {
                val entryList = userParameterList
                    .mapIndexed { i, it ->
                        Entry(i.toFloat(), it.height, it.date)
                    }.toList()
                val lineDataSet = toLineDataSet(
                    entryList,
                    parameter,
                    Color.GREEN,
                    4F,
                    15f,
                    Color.WHITE
                )
                data.add(lineDataSet)
            }
            "Вес" -> {
                val entryList = userParameterList
                    .mapIndexed { i, it ->
                        Entry(i.toFloat(), it.weight, it.date)
                    }.toList()
                val lineDataSet = toLineDataSet(
                    entryList,
                    parameter,
                    Color.GREEN,
                    4F,
                    15f,
                    Color.WHITE
                )
                data.add(lineDataSet)
            }
            "ИМТ" -> {
                val entryList = userParameterList
                    .mapIndexed { i, it ->
                        Entry(i.toFloat(), it.getMassIndex(), it.date)
                    }.toList()
                val deficit = ArrayList<Entry>()
                val normal = ArrayList<Entry>()
                val excess = ArrayList<Entry>()
                val firstDegreeObesity = ArrayList<Entry>()
                val secondDegreeObesity = ArrayList<Entry>()
                for (i in 0..userParameterList.size) {
                    deficit.add(Entry(i.toFloat(), DEFICIT))
                    normal.add(Entry(i.toFloat(), NORMAL))
                    excess.add(Entry(i.toFloat(), EXCESS))
                    firstDegreeObesity.add(Entry(i.toFloat(), FIRST_DEGREE_OBESITY))
                    secondDegreeObesity.add(Entry(i.toFloat(), SECOND_DEGREE_OBESITY))
                }
                val lineDataSet = toLineDataSet(
                    entryList,
                    parameter,
                    Color.GREEN,
                    4F,
                    15f,
                    Color.WHITE
                )
                val deficitLine = toLineDataSet(deficit, "дефицит", Color.BLUE, 1F, 0f, Color.BLUE)
                data.add(deficitLine)
                val normalLine = toLineDataSet(normal, "норма", Color.GREEN, 1F, 0f, Color.GREEN)
                data.add(normalLine)
                val excessLine = toLineDataSet(excess, "избыток", Color.YELLOW, 1F, 0f, Color.YELLOW)
                data.add(excessLine)
                val firstDegreeObesityLine = toLineDataSet(
                    firstDegreeObesity,
                    "1 ст. ожирения",
                    Color.MAGENTA,
                    1F,
                    0f,
                    Color.MAGENTA
                )
                data.add(firstDegreeObesityLine)
                val secondDegreeObesityLine = toLineDataSet(
                    secondDegreeObesity,
                    "2 ст. ожирения",
                    Color.RED,
                    1F,
                    0f,
                    Color.RED
                )
                data.add(secondDegreeObesityLine)
                data.add(lineDataSet)
            }
        }
        return LineData(data)
    }

    private fun toLineDataSet(
        entryList: List<Entry>,
        parameter: String,
        color: Int,
        lineWidth: Float,
        valueTextSize: Float,
        circleColor: Int
    ): LineDataSet {
        val dataSet = LineDataSet(entryList, parameter)
        dataSet.lineWidth = lineWidth
        dataSet.color = color
        dataSet.setDrawCircleHole(false)
        dataSet.setDrawCircles(false)
        dataSet.setCircleColor(circleColor)
        dataSet.valueTextSize = valueTextSize
        return dataSet
    }

}