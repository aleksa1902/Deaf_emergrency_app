package com.example.emergrency_app.police.data

import com.google.firebase.firestore.GeoPoint

data class TerroristData(
    val pretnja: String = "",
    val opisOsoba: String = "",
    val tacnaLokacija: String = "",
    val sumnjivaSituacija: String = "",
    val svedoci: String = "",
    var geografskaLokacija: GeoPoint? = null
)
