package com.example.healthdiary.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.healthdiary.R
import com.example.healthdiary.ui.parameters.ParametersFragment

class MainFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val but1: ImageButton = root.findViewById(R.id.imageButton1)
        val but2: ImageButton = root.findViewById(R.id.imageButton2)
        val but3: ImageButton = root.findViewById(R.id.imageButton3)
        val but4: ImageButton = root.findViewById(R.id.imageButton4)
        val but5: ImageButton = root.findViewById(R.id.imageButton5)
        val but6: ImageButton = root.findViewById(R.id.imageButton6)

        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        Navigation.setViewNavController(but1, navController)
        Navigation.setViewNavController(but2, navController)
        Navigation.setViewNavController(but3, navController)
        Navigation.setViewNavController(but4, navController)
        Navigation.setViewNavController(but5, navController)
        Navigation.setViewNavController(but6, navController)

        but1.setOnClickListener{
            navController.navigate(R.id.nav_parameters)
        }

        but2.setOnClickListener{
            navController.navigate(R.id.nav_medicaments)
        }

        but3.setOnClickListener{
            navController.navigate(R.id.nav_statistics)
        }

        but4.setOnClickListener{
            navController.navigate(R.id.nav_watertrack)
        }

        but5.setOnClickListener{
            navController.navigate(R.id.nav_planning)
        }

        but6.setOnClickListener{
            navController.navigate(R.id.nav_recommendations)
        }
        return root
    }
}