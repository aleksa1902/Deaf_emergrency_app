package com.example.emergrency_app.firefighters.data

import com.google.firebase.firestore.GeoPoint

data class RescureDangerData(
    val prirodaOpasnosti: String = "",
    val brojOsobaZivotinja: String = "",
    val tacnaLokacija: String = "",
    val zarobljeniPovrede: String = "",
    val situacijaPodrucje: String = "",
    val geografskaLokacija: GeoPoint? = null
)
