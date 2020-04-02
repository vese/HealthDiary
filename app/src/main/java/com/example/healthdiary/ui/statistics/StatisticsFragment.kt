package com.example.healthdiary.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import com.example.healthdiary.db.DBHelper
import com.example.healthdiary.db.UserParameters
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_statistics, container, false)
        val parametersChart = root.findViewById<LineChart>(R.id.parameters_chart)

        val dbHelper = DBHelper(this.requireContext())
        val userParameterList = dbHelper.getUserParametersList()

        if (userParameterList.isNotEmpty()) {
            val lineData = mapToLineData(userParameterList)
            parametersChart.data = lineData
        }
        parametersChart.setNoDataText("Enter your data at ${getString(R.string.menu_parameters)}")
        parametersChart.setNoDataTextColor(Color.BLACK)
        parametersChart.invalidate()
        return root
    }

    private fun mapToLineData(userParameterList: List<UserParameters>): LineData {
        val weightData = ArrayList<Entry>()
        userParameterList.forEachIndexed { i, it ->
            weightData.add(Entry(i.toFloat(), it.weight))
        }
        val heightData = ArrayList<Entry>()
        userParameterList.forEachIndexed { i, it ->
            heightData.add(Entry(i.toFloat(), it.height))
        }
        val weightDataSet = LineDataSet(weightData, "weight")
        weightDataSet.lineWidth = 4F
        weightDataSet.color = Color.GREEN
        weightDataSet.setDrawCircleHole(false)
        weightDataSet.setCircleColor(Color.WHITE)
        weightDataSet.valueTextSize = 15f

        val heightDataSet = LineDataSet(heightData, "height")
        heightDataSet.lineWidth = 4F
        heightDataSet.color = Color.CYAN
        heightDataSet.setDrawCircleHole(false)
        heightDataSet.setCircleColor(Color.WHITE)
        heightDataSet.valueTextSize = 15f

        val data = ArrayList<ILineDataSet>()
        data.add(weightDataSet)
        data.add(heightDataSet)
        return LineData(data)
    }

}