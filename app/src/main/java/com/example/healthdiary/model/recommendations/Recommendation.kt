package com.example.healthdiary.model.recommendations

import java.io.Serializable

data class Recommendation(
    val title: String,
    val text: String
) : Serializable