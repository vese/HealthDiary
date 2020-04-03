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
        var dailyRate = calculateDailyRate(userWeight, userSex, container!!.context)
        updateScreen(dailyRate, currentIntake, root);

        val radioGroup: RadioGroup = root.findViewById(R.id.radioGroup)
        val inputAmountText: EditText = root.findViewById(R.id.inputAmountText)
        val addButton: Button = root.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val addAmountStr = inputAmountText.text.toString()
            if (addAmountStr.isNullOrEmpty())
            {
                Toast.makeText(container!!.context, "Введите количество!", Toast.LENGTH_LONG).show()
            }
            else
            {
                inputAmountText.setText("")
                val mult = when {
                    radioGroup.checkedRadioButtonId == R.id.waterRadioButton -> 1f
                    radioGroup.checkedRadioButtonId == R.id.juiceRadioButton -> 0.8f
                    radioGroup.checkedRadioButtonId == R.id.milkRadioButton -> 0.8f
                    radioGroup.checkedRadioButtonId == R.id.coffeeRadioButton -> 0.1f
                    radioGroup.checkedRadioButtonId == R.id.teaRadioButton -> 0.1f
                    else -> 0f
                }
                currentIntake += (addAmountStr.toInt() * mult).toInt()
                updateScreen(dailyRate, currentIntake, root);
                dbHandler.updateWaterTrackParameters(WaterTrackParameters(currentIntake))
            }
        }

        return root
    }

    private fun updateScreen(dailyRate: Int, currentIntake: Int, root: View)
    {
        val waterProgressBar: ProgressBar = root.findViewById(R.id.circularProgressbar)
        val percentTextView: TextView = root.findViewById(R.id.percentTextView)
        val normTextView: TextView = root.findViewById(R.id.normTextView)

        var progressPercent = (currentIntake.toFloat() / dailyRate * 100).toInt();
        waterProgressBar.setProgress(progressPercent);
        percentTextView.text = "${progressPercent.toString()} %";
        normTextView.text = "${currentIntake.toString()} / ${dailyRate.toString()} мл"
    }

    private fun calculateDailyRate(userWeight: Float?, userSex: String?, context: Context): Int {
        if (userWeight == null || userSex == null) {
            Toast.makeText(context, "Недостаточно данных!", Toast.LENGTH_LONG).show()
            return 2000;
        } else {
            if (userSex == "Male") {
                return (35f * userWeight).toInt();
            } else {
                return (31f * userWeight).toInt();
            }
        }
    }
}