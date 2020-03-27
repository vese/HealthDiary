package com.example.healthdiary.ui.recommendations

import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.healthdiary.R
import com.example.healthdiary.model.recommendations.Recommendation
import com.example.healthdiary.util.AbstractListAdapter

class RecommendationListAdapter(context: Context, objects: List<Recommendation>) :
    AbstractListAdapter<Recommendation>(context, R.layout.item_list_element, objects) {

    override fun createViewFromResource(v: View, position: Int): View {
        val title = v.findViewById<TextView>(R.id.recommendation_title)
        val recommendation = getItem(position)!!
        title.text = recommendation.title
        return v
    }
}