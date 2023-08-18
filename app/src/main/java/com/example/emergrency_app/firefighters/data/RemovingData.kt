package com.example.emergrency_app.firefighters.data

import com.google.firebase.firestore.GeoPoint

data class RemovingData(
    val vrstaStvari: String = "",
    val tacnaLokacija: String = "",
    val opasnostEksplozije: String = "",
    val sigurnostOkoline: String = "",
    val povredjeneOsobe: String = "",
    var geografskaLokacija: GeoPoint? = null
)
