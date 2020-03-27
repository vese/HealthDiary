package com.example.healthdiary.service.recommendations

import com.example.healthdiary.model.recommendations.Recommendation

interface RecommendationsService {

    fun getRecommendations(): List<Recommendation>

}