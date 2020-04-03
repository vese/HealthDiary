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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class StatisticsFragment : Fragment() {

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
        val userParameterList = getSortedData(dbHelper)

        val selectedParameter = spinner.selectedItem.toString()
        if (userParameterList.isNotEmpty()) {
            val lineData = mapToLineData(userParameterList, selectedParameter)
            parametersChart.data = lineData
        }
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
                if (userParameterList.isNotEmpty()) {
                    val lineData = mapToLineData(userParameterList, elements[position])
                    parametersChart.data = lineData
                    parametersChart.invalidate()
                }
            }
        }
        parametersChart.setNoDataText("Enter your data at ${getString(R.string.menu_parameters)}")
        parametersChart.setNoDataTextColor(Color.BLACK)
        parametersChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        parametersChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String =
                userParameterList[value.toInt()].date
        }
        parametersChart.invalidate()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSortedData(dbHelper: DBHelper) =
        dbHelper.getUserParametersList().sortedBy {
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

    private fun mapToLineData(
        userParameterList: List<UserParameters>,
        parameter: String
    ): LineData {
        val entryList = ArrayList<Entry>()

        userParameterList
            .forEachIndexed { i, it ->
                when (parameter) {
                    "Рост" -> entryList.add(Entry(i.toFloat(), it.height, it.date))
                    "Вес" -> entryList.add(Entry(i.toFloat(), it.weight, it.date))
                }
            }

        val dataSet = LineDataSet(entryList, parameter)
        dataSet.lineWidth = 4F
        dataSet.color = Color.GREEN
        dataSet.setDrawCircleHole(false)
        dataSet.setCircleColor(Color.WHITE)
        dataSet.valueTextSize = 15f
        val data = ArrayList<ILineDataSet>()
        data.add(dataSet)
        return LineData(data)
    }

}