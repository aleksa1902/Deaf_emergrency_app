package com.example.emergrency_app.police.data

import com.google.firebase.firestore.GeoPoint

data class CarAccidentData(
    val lokacija: String = "",
    val brojAutomobila: String = "",
    val povrede: String = "",
    val informacijeVozaci: String = "",
    val sumnjiveAktivnosti: String = "",
    val geografskaLokacija: GeoPoint? = null
)
