package com.example.healthdiary.model.medicaments

import java.io.Serializable

data class Medicament(
    val name: String,
    val price: String,
    val dosage: String,
    val manufacturer: String
) : Serializable