package com.example.emergrency_app.firefighters.data

import com.google.firebase.firestore.GeoPoint

data class GasData(
    val znakoviCurenja: String = "",
    val tacnaLokacija: String = "",
    val ljudiOkolina: String = "",
    val situacijaPodrucje: String = "",
    val dodatneInformacije: String = "",
    val geografskaLokacija: GeoPoint? = null
)
