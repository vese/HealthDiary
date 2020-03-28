package com.example.healthdiary.service.medicaments

import com.example.healthdiary.model.medicaments.Medicament

interface MedicamentsService {

    fun getMedicaments(): List<Medicament>

}