package com.example.healthdiary.ui.medicaments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.example.healthdiary.model.medicaments.Medicament
import com.example.healthdiary.service.ServiceHolder

class MedicamentsListFragment : ListFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AsyncTask.execute {
            val medicamentsService = ServiceHolder.medicamentsService
            val result = medicamentsService.getMedicaments()
            activity!!.runOnUiThread {
                listAdapter =
                    MedicamentsListAdapter(context!!, result)
            }
        }
        listAdapter =
            MedicamentsListAdapter(context!!, emptyList())
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val medicament = l.getItemAtPosition(position) as Medicament
        val intent = Intent(activity, MedicamentActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("medicament", medicament)
        intent.putExtra("bundle", bundle)
        context!!.startActivity(intent)
    }
}