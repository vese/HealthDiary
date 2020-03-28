package com.example.healthdiary.ui.medicaments

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.healthdiary.R
import com.example.healthdiary.model.medicaments.Medicament
import com.example.healthdiary.util.AbstractListAdapter

class MedicamentsListAdapter(context: Context, objects: List<Medicament>) :
    AbstractListAdapter<Medicament>(context, R.layout.item_list_element, objects) {

    override fun createViewFromResource(v: View, position: Int): View {
        val title = v.findViewById<TextView>(R.id.recommendation_title)
        val recommendation = getItem(position)!!
        title.text = recommendation.name
        return v
    }
}