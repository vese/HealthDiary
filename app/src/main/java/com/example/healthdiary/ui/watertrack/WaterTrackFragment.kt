package com.example.healthdiary.ui.watertrack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import com.example.healthdiary.db.DBHelper
import com.example.healthdiary.db.WaterTrackParameters
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WaterTrackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_watertrack, container, false)

        val dbHandler = DBHelper(container!!.context, null)
        var currentIntake = dbHandler.getLastWaterTrackParameters()?.currentIntake
        if (currentIntake == null) {
            currentIntake = 0
        }
        val userWeight = dbHandler.getLastUserParameters()?.weight
        val userSex = dbHandler.getUserParameter("sex")
        val dailyRate = calculateDailyRate(userWeight, userSex, container.context)
        updateScreen(dailyRate, currentIntake, root)

        val radioGroup: RadioGroup = root.findViewById(R.id.radioGroup)
        val inputAmountText: EditText = root.findViewById(R.id.inputAmountText)
        val addButton: Button = root.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val addAmountStr = inputAmountText.text.toString()
            if (addAmountStr.isEmpty())
            {
                Toast.makeText(container.context, "Введите количество!", Toast.LENGTH_LONG).show()
            }
            else
            {
                inputAmountText.setText("")
                val mult = when (radioGroup.checkedRadioButtonId) {
                    R.id.waterRadioButton -> 1f
                    R.id.juiceRadioButton -> 0.8f
                    R.id.milkRadioButton -> 0.8f
                    R.id.coffeeRadioButton -> 0.1f
                    R.id.teaRadioButton -> 0.1f
                    else -> 0f
                }
                currentIntake += (addAmountStr.toInt() * mult).toInt()
                updateScreen(dailyRate, currentIntake, root)
                dbHandler.updateWaterTrackParameters(WaterTrackParameters(currentIntake))
            }
        }


        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        val dark: ImageButton = root.findViewById(R.id.dark)
        val helpImage: ImageView = root.findViewById(R.id.help_image)
        fab.setOnClickListener {
            val visibility = if (dark.visibility == View.GONE) {
                View.VISIBLE
            } else {
                View.GONE
            }
            dark.visibility = visibility
            helpImage.visibility = visibility
        }

        dark.setOnClickListener {
            dark.visibility = View.GONE
            helpImage.visibility = View.GONE
        }

        return root
    }

    private fun updateScreen(dailyRate: Int, currentIntake: Int, root: View)
    {
        val waterProgressBar: ProgressBar = root.findViewById(R.id.circularProgressbar)
        val percentTextView: TextView = root.findViewById(R.id.percentTextView)
        val normTextView: TextView = root.findViewById(R.id.normTextView)

        val progressPercent = (currentIntake.toFloat() / dailyRate * 100).toInt()
        waterProgressBar.progress = progressPercent
        percentTextView.text = getString(R.string.progress_percent, progressPercent)
        normTextView.text = getString(R.string.water_current, currentIntake, dailyRate)
    }

    private fun calculateDailyRate(userWeight: Float?, userSex: String?, context: Context): Int {
        return if (userWeight == null || userSex == null) {
            Toast.makeText(context, getString(R.string.not_enough_data), Toast.LENGTH_LONG).show()
            2000
        } else {
            if (userSex == getString(R.string.male)) {
                (35f * userWeight).toInt()
            } else {
                (31f * userWeight).toInt()
            }
        }
    }
}