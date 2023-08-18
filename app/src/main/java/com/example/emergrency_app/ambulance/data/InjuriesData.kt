package com.example.emergrency_app.ambulance.data

import com.google.firebase.firestore.GeoPoint

data class InjuriesData(
    val vrstaPovrede: String = "",
    val tezinaPovrede: String = "",
    val svest: String = "",
    val krvarenje: String = "",
    val pokretljivostOsobe: String = "",
    var geografskaLokacija: GeoPoint? = null
)
