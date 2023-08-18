package com.example.emergrency_app.firefighters.data

import com.google.firebase.firestore.GeoPoint

data class FireData(
    val lokacija: String = "",
    val staGori: String = "",
    val zaglavljeneOsobe: String = "",
    val opasnost: String = "",
    val opasneMaterije: String = "",
    var geografskaLokacija: GeoPoint? = null
)
