package com.example.healthdiary.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.healthdiary.R

class MainFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val butParams: ImageButton = root.findViewById(R.id.imageButtonParams)
        val butMeds: ImageButton = root.findViewById(R.id.imageButtonMeds)
        val butStats: ImageButton = root.findViewById(R.id.imageButtonStats)
        val butWater: ImageButton = root.findViewById(R.id.imageButtonWater)
        val butPlans: ImageButton = root.findViewById(R.id.imageButtonPlans)
        val butRec: ImageButton = root.findViewById(R.id.imageButtonRec)

        val navController = requireActivity().findNavController(R.id.nav_host_fragment)
        Navigation.setViewNavController(butParams, navController)
        Navigation.setViewNavController(butMeds, navController)
        Navigation.setViewNavController(butStats, navController)
        Navigation.setViewNavController(butWater, navController)
        Navigation.setViewNavController(butPlans, navController)
        Navigation.setViewNavController(butRec, navController)

        butParams.setOnClickListener{
            navController.navigate(R.id.nav_parameters)
        }

        butMeds.setOnClickListener{
            navController.navigate(R.id.nav_medicaments)
        }

        butStats.setOnClickListener{
            navController.navigate(R.id.nav_statistics)
        }

        butWater.setOnClickListener{
            navController.navigate(R.id.nav_watertrack)
        }

        butPlans.setOnClickListener{
            navController.navigate(R.id.nav_planning)
        }

        butRec.setOnClickListener{
            navController.navigate(R.id.nav_recommendations)
        }
        return root
    }
}