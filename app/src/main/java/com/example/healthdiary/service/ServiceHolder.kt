package com.example.healthdiary.service

import com.example.healthdiary.service.medicaments.impl.JsonBinMedicamentsService
import com.example.healthdiary.service.recommendations.impl.MockRecommendationsService

object ServiceHolder {
    val recommendationsService = MockRecommendationsService()
    val medicamentsService = JsonBinMedicamentsService()
}