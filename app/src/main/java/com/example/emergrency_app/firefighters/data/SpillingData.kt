package com.example.emergrency_app.firefighters.data

import com.google.firebase.firestore.GeoPoint

data class SpillingData(
    val stvarIzlivanja: String = "",
    val tacnaLokacija: String = "",
    val opasnostEksplozije: String = "",
    val opsobeOkolina: String = "",
    val dodatneInformacije: String = "",
    var geografskaLokacija: GeoPoint? = null
)
