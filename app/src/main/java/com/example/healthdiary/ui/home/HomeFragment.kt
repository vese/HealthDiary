package com.example.healthdiary.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import com.example.healthdiary.db.DBHelper
import com.example.healthdiary.db.UserParameters
import kotlin.math.pow

class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val dbHandler = DBHelper(container!!.context, null)
        val inputWeight: EditText = root.findViewById(R.id.input_weight)
        val inputHeight: EditText = root.findViewById(R.id.input_height)
        val lastUserParameters = dbHandler.getLastUserParameters()
        inputWeight.setText(lastUserParameters.weight.toString())
        inputHeight.setText(lastUserParameters.height.toString())
        val hintText: TextView = root.findViewById(R.id.hint_text)
        hintText.text = getHint(lastUserParameters)
        val userParametersList = dbHandler.getUserParametersList()
        val resultsTable: TableLayout = root.findViewById(R.id.results_table)
        fillTable(userParametersList, resultsTable, container!!.context)

        val saveButton: Button = root.findViewById(R.id.button_save)
        saveButton.setOnClickListener {
            val weight = inputWeight.text.toString()
            val height = inputHeight.text.toString()
            if (weight == "" || height == "") {
                Toast.makeText(container!!.context, "Fill empty inputs!", Toast.LENGTH_LONG).show()
            } else {
                val userParameters = UserParameters(weight.toFloat(), height.toFloat())
                dbHandler.insertUserParameter(userParameters)
                Toast.makeText(container!!.context, "New values were saved!", Toast.LENGTH_LONG)
                    .show()

                hintText.setText(getHint(userParameters))
                val result = dbHandler.getUserParametersList()
                fillTable(result, resultsTable, container!!.context)
            }
        }
        return root
    }

    private fun getHint(userParameters: UserParameters): String {
        val massIndex = userParameters.weight / (userParameters.height / 100f).pow(2)
        return when {
            massIndex < 18.5f -> "The deficit of body weight"
            massIndex < 25 -> "Normal body weight"
            massIndex < 30 -> "Excess body weight"
            massIndex < 35 -> "1st degree obesity"
            massIndex < 40 -> "2d degree obesity"
            else -> "3d degree obesity"
        }
    }

    private fun fillTable(userParametersList: ArrayList<UserParameters>, resultsTable: TableLayout, context: Context) {
        val tableRow = TableRow(context)
        resultsTable.removeAllViews()
        tableRow.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tableRow.addView(fillCell("Date", context))
        tableRow.addView(fillCell("Weight", context))
        tableRow.addView(fillCell("Height", context))
        resultsTable.addView(tableRow)
        for (userParameters in userParametersList) {
            resultsTable.addView(fillRow(userParameters, context))
        }
    }


    private fun fillRow(userParameters: UserParameters, context: Context): TableRow {
        val tableRow = TableRow(context)
        tableRow.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tableRow.addView(fillCell(userParameters.date, context))
        tableRow.addView(fillCell(userParameters.weight.toString(), context))
        tableRow.addView(fillCell(userParameters.height.toString(), context))
        return tableRow
    }

    private fun fillCell(value: String, context: Context): TextView {
        val cell = TextView(context)
        cell.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        cell.text = value
        return cell
    }
}