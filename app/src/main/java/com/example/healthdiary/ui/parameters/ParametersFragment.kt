package com.example.healthdiary.ui.parameters

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import com.example.healthdiary.db.DBHelper
import com.example.healthdiary.db.UserParameter
import com.example.healthdiary.db.UserParameters
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

class ParametersFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_parameters, container, false)

        //params controls
        val inputWeight: EditText = root.findViewById(R.id.input_weight)
        val inputHeight: EditText = root.findViewById(R.id.input_height)
        val hintText: TextView = root.findViewById(R.id.hint_text)
        val resultsTable: TableLayout = root.findViewById(R.id.results_table)
        val ageText: TextView = root.findViewById(R.id.age_text)
        val sexSpinner: Spinner = root.findViewById(R.id.sex_spinner)

        //db
        val dbHandler = DBHelper(container!!.context, null)
        val lastUserParameters = dbHandler.getLastUserParameters()
        if (lastUserParameters != null) {
            inputWeight.setText(lastUserParameters.weight.toString())
            inputHeight.setText(lastUserParameters.height.toString())
            val userParametersList = dbHandler.getUserParametersList()
            fillTable(userParametersList, resultsTable, container!!.context)
        }
        val c = Calendar.getInstance()
        var birthDate = ""
        var birthYear: Int = c.get(Calendar.YEAR)
        var birthMonth = c.get(Calendar.MONTH)
        var birthDay = c.get(Calendar.DAY_OF_MONTH)
        val userParameterList = dbHandler.getUserParameterList()
        for (userParameter in userParameterList) {
            if (userParameter.name == "birthDate") {
                birthDate = userParameter.value
                val dateParams = birthDate.split(".").toTypedArray()
                birthDay = dateParams[0].toInt()
                birthMonth = dateParams[1].toInt()
                birthYear = dateParams[2].toInt()
                var age = getAge(birthDay, birthMonth, birthYear)
                if (age >= 0) {
                    ageText.text = "Your age is $age"
                }
            } else if (userParameter.name == "sex") {
                val adapter = ArrayAdapter(container!!.context, R.layout.fragment_parameters ,container!!.context.resources.getStringArray(R.array.sex_list))
                sexSpinner.setSelection(adapter.getPosition(userParameter.value))
            } else if (userParameter.name == "massIndex") {
                hintText.text = getHint(userParameter.value.toFloat())
            }
        }

        //birth date
        val chooseDateButton: Button = root.findViewById(R.id.button_choose_date)
        chooseDateButton.setOnClickListener {
            val dpd = DatePickerDialog(container!!.context, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                var age = getAge(dayOfMonth, monthOfYear, year)
                if (age >= 0) {
                    ageText.text = "Your age is $age"
                }

                birthDate = "$dayOfMonth.$monthOfYear.$year"

                birthYear = year
                birthMonth = monthOfYear
                birthDay = dayOfMonth
            }, birthYear, birthMonth, birthDay)

            dpd.show()
        }

        //save
        val saveButton: Button = root.findViewById(R.id.button_save)
        saveButton.setOnClickListener {
            val weight = inputWeight.text.toString()
            val height = inputHeight.text.toString()
            if (weight == "" || height == "" || birthDate == "") {
                Toast.makeText(container!!.context, "Fill empty inputs!", Toast.LENGTH_LONG).show()
            } else {
                dbHandler.updateUserParameter(UserParameter("birthDate", birthDate))

                dbHandler.updateUserParameter(UserParameter("sex", sexSpinner.selectedItem.toString()))

                val userParameters = UserParameters(weight.toFloat(), height.toFloat())
                dbHandler.insertUserParameters(userParameters)

                val massIndex = getMassIndex(userParameters)
                dbHandler.updateUserParameter(UserParameter("massIndex", massIndex.toString()))
                hintText.text = getHint(massIndex)

                val result = dbHandler.getUserParametersList()
                fillTable(result, resultsTable, container!!.context)

                Toast.makeText(container!!.context, "New values were saved!", Toast.LENGTH_LONG)
                    .show()
            }
        }

        return root
    }

    private fun getAge(day: Int, month: Int, year: Int): Int {
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
        var age = currentYear - year
        if (currentMonth < month || currentMonth == month && currentDay < day)
        {
            age--
        }
        return age
    }

    private fun getMassIndex(userParameters: UserParameters): Float {
        return userParameters.weight / (userParameters.height / 100f).pow(2)
    }

    private fun getHint(massIndex: Float): String {
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