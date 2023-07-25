package com.example.emergrency_app.firefighters.data

import com.google.firebase.firestore.GeoPoint

data class InterventionsData(
    val tehnickiProblem: String = "",
    val tacnaLokacija: String = "",
    val zarobljeniPovrede: String = "",
    val situacijaPodrucje: String = "",
    val medicinskaPomoc: String = "",
    val geografskaLokacija: GeoPoint? = null
)
