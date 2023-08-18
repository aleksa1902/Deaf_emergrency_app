package com.example.emergrency_app.ambulance.data

import com.google.firebase.firestore.GeoPoint

data class PoisoningData(
    val stvarTrovanja: String = "",
    val vremeTrovanja: String = "",
    val osecajOsobe: String = "",
    val svest: String = "",
    val vidljiviZnakovi: String = "",
    var geografskaLokacija: GeoPoint? = null
)
