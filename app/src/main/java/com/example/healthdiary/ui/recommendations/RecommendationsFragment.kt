package com.example.healthdiary.ui.recommendations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.healthdiary.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecommendationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_recommendations, container, false)

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
}