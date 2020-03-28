package com.example.healthdiary.service.medicaments.impl

import com.example.healthdiary.model.medicaments.Medicament
import com.example.healthdiary.service.medicaments.MedicamentsService

class MockMedicamentsService : MedicamentsService {

    override fun getMedicaments(): List<Medicament> {
        return listOf(
            Medicament(
                "Глицин",
                "75 р",
                "100 мг",
                "Биотики"
            ),
            Medicament(
                "Кагоцел",
                "255 р",
                "35 мг",
                "Ниармедик Плюс ООО"
            ),
            Medicament(
                "Полидекса с фенилэфрином",
                "800 р",
                "15мл",
                "Лаборатории Бушара-Рекорд"
            )
        )
    }
}