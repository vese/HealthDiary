package com.example.healthdiary.ui.recommendations

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.example.healthdiary.model.recommendations.Recommendation
import com.example.healthdiary.service.ServiceHolder

class RecommendationsListFragment : ListFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recommendationsService = ServiceHolder.recommendationsService
        listAdapter =
            RecommendationListAdapter(context!!, recommendationsService.getRecommendations())
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val recommendation = l.getItemAtPosition(position) as Recommendation
        val intent = Intent(activity, RecommendationActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("recommendation", recommendation)
        intent.putExtra("bundle", bundle)
        context!!.startActivity(intent)
    }
}