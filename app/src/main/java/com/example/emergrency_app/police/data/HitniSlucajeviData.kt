package com.example.emergrency_app.police.data

import com.google.firebase.firestore.GeoPoint

data class HitniSlucajeviData(
    val lokacija: String = "",
    val prirodaSituacije: String = "",
    val povrede: String = "",
    val brojOsoba: String = "",
    val opisPocinitelja: String = "",
    var geografskaLokacija: GeoPoint? = null
)
