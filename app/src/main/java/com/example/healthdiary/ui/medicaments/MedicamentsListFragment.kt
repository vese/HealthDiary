package com.example.healthdiary.ui.medicaments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.ListFragment
import com.example.healthdiary.R
import com.example.healthdiary.db.DBHelper
import com.example.healthdiary.model.medicaments.Medicament
import com.example.healthdiary.service.ServiceHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MedicamentsListFragment : ListFragment() {

    private val medicamentsService = ServiceHolder.medicamentsService

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AsyncTask.execute {
            val result = getMedicaments()
            requireActivity().runOnUiThread {
                listAdapter =
                    MedicamentsListAdapter(requireContext(), result)
            }
        }
        listAdapter =
            MedicamentsListAdapter(requireContext(), emptyList())

        val fab: FloatingActionButton = requireActivity().findViewById(R.id.fab)
        val dark: ImageButton = requireActivity().findViewById(R.id.dark)
        val helpImage: ImageView = requireActivity().findViewById(R.id.help_image)
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
    }

    private fun getMedicaments(): List<Medicament> {
        val medicaments = medicamentsService.getMedicaments()
        val dbHelper = DBHelper(requireContext())
        if (medicaments.isEmpty()) {
            activity?.runOnUiThread{
                Toast.makeText(
                    requireContext(),
                    "Medicaments were not found in internet",
                    Toast.LENGTH_LONG
                ).show()
            }
            return dbHelper.getMedicaments()
        } else {
            medicaments.forEach { dbHelper.saveMedicament(it) }
        }
        return medicaments
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        val medicament = l.getItemAtPosition(position) as Medicament
        val intent = Intent(activity, MedicamentActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("medicament", medicament)
        intent.putExtra("bundle", bundle)
        requireContext().startActivity(intent)
    }
}