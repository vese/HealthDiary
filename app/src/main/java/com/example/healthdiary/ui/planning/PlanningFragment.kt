package com.example.healthdiary.ui.planning

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import com.example.healthdiary.db.DBHelper
import com.example.healthdiary.db.Plan
import com.example.healthdiary.model.medicaments.Medicament
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KProperty1

class PlanningFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dateFormat =
            SimpleDateFormat(getString(R.string.date_format), Locale(getString(R.string.ru)))
        val titleDateFormat =
            SimpleDateFormat(getString(R.string.title_date_format), Locale(getString(R.string.ru)))
        val root = inflater.inflate(R.layout.fragment_planning, container, false)
        val dbHandler = DBHelper(container!!.context, null)

        val showSwitch: Switch = root.findViewById(R.id.show_switch)
        val typeSpinner: Spinner = root.findViewById(R.id.spinner_type)
        val medSpinner: Spinner = root.findViewById(R.id.spinner_medicament)
        val monthText: TextView = root.findViewById(R.id.month_text)
        val compactCalendar: CompactCalendarView = root.findViewById(R.id.compactcalendar_view)

        medSpinner.adapter = ArrayAdapter(
            container.context,
            android.R.layout.simple_spinner_dropdown_item,
            dbHandler.getMedicaments().listOfField(Medicament::name)
        )

        compactCalendar.setUseThreeLetterAbbreviation(true)
        monthText.text =
            titleDateFormat.format(compactCalendar.firstDayOfCurrentMonth)

        var chosenDate = dateFormat.format(Date())

        var dayPlansCount = loadPlans(
            root,
            container.context,
            dbHandler,
            chosenDate
        )

        val plans = dbHandler.getPlansList()
        for (plan in plans) {
            val timestamp = dateFormat.parse(plan.date)!!.toInstant().epochSecond * 1000
            compactCalendar.addEvent(Event(Color.GREEN, timestamp))
        }

        setVisibility(root, showSwitch.isChecked, dayPlansCount)

        showSwitch.setOnCheckedChangeListener { _, checked ->
            setVisibility(root, checked, dayPlansCount)
        }

        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setMedVisibility(root, typeSpinner.selectedItem.toString())
            }
        }

        compactCalendar.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                chosenDate = dateFormat.format(dateClicked)
                dayPlansCount = loadPlans(root, container.context, dbHandler, chosenDate)
                setVisibility(root, showSwitch.isChecked, dayPlansCount)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                monthText.text =
                    titleDateFormat.format(compactCalendar.firstDayOfCurrentMonth)
            }
        })

        val addButton: Button = root.findViewById(R.id.add_button)
        addButton.setOnClickListener {
            var description = typeSpinner.selectedItem.toString()
            if (description == getString(R.string.medicine_taking)) {
                description = "$description: ${medSpinner.selectedItem}"
            }
            val plan = Plan(chosenDate, description)
            dbHandler.insertPlan(plan)
            dayPlansCount = loadPlans(root, container.context, dbHandler, chosenDate)
            val timestamp = dateFormat.parse(chosenDate)!!.toInstant().epochSecond * 1000
            compactCalendar.addEvent(Event(Color.GREEN, timestamp))
            Toast.makeText(container.context, getString(R.string.new_plan_added), Toast.LENGTH_LONG)
                .show()
        }

        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        val dark: ImageButton = root.findViewById(R.id.dark)
        val helpImage: ImageView = root.findViewById(R.id.help_image)
        val helpAddingImage: ImageView = root.findViewById(R.id.help_adding_image)
        fab.setOnClickListener {
            val visibility = if (dark.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
            dark.visibility = visibility
            if (showSwitch.isChecked) {
                helpAddingImage.visibility = visibility
                helpImage.visibility = View.GONE
            } else {
                helpAddingImage.visibility = View.GONE
                helpImage.visibility = visibility
            }
        }

        dark.setOnClickListener {
            dark.visibility = View.GONE
            helpImage.visibility = View.GONE
            helpAddingImage.visibility = View.GONE
        }

        return root
    }

    private inline fun <reified T, Y> List<T>.listOfField(property: KProperty1<T, Y>):List<Y> {
        val yy = ArrayList<Y>()
        this.forEach { t: T ->
            yy.add(property.get(t))
        }
        return yy
    }

    private fun loadPlans(root: View, context: Context, dbHandler: DBHelper, date: String): Int {
        val plans = dbHandler.getPlansByDate(date)
        val resultsTable: TableLayout = root.findViewById(R.id.results_table)
        fillTable(plans, resultsTable, context)
        return plans.count()
    }

    private fun fillTable(plans: ArrayList<Plan>, resultsTable: TableLayout, context: Context) {
        val tableRow = TableRow(context)
        resultsTable.removeAllViews()
        tableRow.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tableRow.addView(fillCell(getString(R.string.date), context))
        tableRow.addView(fillCell(getString(R.string.planned_event), context))
        resultsTable.addView(tableRow)
        for (plan in plans) {
            resultsTable.addView(fillRow(plan, context))
        }
    }

    private fun fillRow(plan: Plan, context: Context): TableRow {
        val tableRow = TableRow(context)
        tableRow.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tableRow.addView(fillCell(plan.date, context))
        tableRow.addView(fillCell(plan.description, context))
        return tableRow
    }

    private fun fillCell(value: String, context: Context): TextView {
        val cell = TextView(context)
        cell.layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        cell.text = value
        return cell
    }

    private fun setMedVisibility(root: View, selectedType: String) {
        val medSpinner: Spinner = root.findViewById(R.id.spinner_medicament)
        if (selectedType == getString(R.string.medicine_taking)) {
            medSpinner.visibility = View.VISIBLE
        } else {
            medSpinner.visibility = View.GONE
        }
    }

    private fun setVisibility(root: View, checked: Boolean, plansCount: Int) {
        val plansText: TextView = root.findViewById(R.id.plans_text)
        val typeSpinner: Spinner = root.findViewById(R.id.spinner_type)
        val medSpinner: Spinner = root.findViewById(R.id.spinner_medicament)
        val addButton: Button = root.findViewById(R.id.add_button)
        val table: TableLayout = root.findViewById(R.id.results_table)
        if (checked) {
            plansText.visibility = View.GONE
            table.visibility = View.GONE
            typeSpinner.visibility = View.VISIBLE
            setMedVisibility(root, typeSpinner.selectedItem.toString())
            addButton.visibility = View.VISIBLE

        } else {
            if (plansCount == 0) {
                plansText.visibility = View.VISIBLE
                table.visibility = View.GONE
            } else {
                plansText.visibility = View.GONE
                table.visibility = View.VISIBLE
            }
            typeSpinner.visibility = View.GONE
            medSpinner.visibility = View.GONE
            addButton.visibility = View.GONE
        }
    }
}